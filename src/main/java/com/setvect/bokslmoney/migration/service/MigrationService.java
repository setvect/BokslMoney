package com.setvect.bokslmoney.migration.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.repository.CategoryRepository;
import com.setvect.bokslmoney.hab.vo.AccountVo;
import com.setvect.bokslmoney.hab.vo.CategoryVo;
import com.setvect.bokslmoney.hab.vo.KindType;

@Service
public class MigrationService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public void run() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		CodeMainVo kindCode = codeMainRepository.findById(CodeKind.KIND_CODE.name()).get();

		try {
			String url = "jdbc:sqlite:./temp/setvect.db";

			// 1. 계좌 코드
			conn = DriverManager.getConnection(url);
			ps = conn.prepareStatement("select  * from t_acctype");
			rs = ps.executeQuery();
			// codeItemRepository.deleteAll();

			while (rs.next()) {
				int key = rs.getInt("at_key");
				String name = rs.getString("at_name");
				int order = rs.getInt("at_order");
				CodeItemVo codeItem = new CodeItemVo();
				codeItem.setCodeItemKey(new CodeItemKey(kindCode, key));
				codeItem.setName(name);
				codeItem.setOrderNo(order);
				codeItemRepository.saveAndFlush(codeItem);
			}
			rs.close();
			ps.close();

			// 2. 계좌 정보
			ps = conn.prepareStatement("select  * from t_account");
			rs = ps.executeQuery();
			// accountRepository.deleteAll();
			while (rs.next()) {
				AccountVo account = new AccountVo();
				account.setAccountSeq(rs.getInt("a_key"));
				account.setName(rs.getString("a_name"));
				account.setAccountNumber(rs.getString("a_number"));
				account.setKindCode(rs.getInt("a_kind"));
				account.setBalance(rs.getInt("a_money"));
				account.setInterestRate(rs.getString("a_rate"));
				account.setTerm(rs.getString("a_month"));
				account.setExpDate(rs.getString("a_outday"));
				account.setMonthlyPay(rs.getString("a_money_month"));
				account.setNote(rs.getString("a_memo"));
				accountRepository.saveAndFlush(account);
			}
			rs.close();
			ps.close();

			// 3. 항목 코드
			ps = conn.prepareStatement("select  * from t_item order by it_key");
			rs = ps.executeQuery();
			Map<Integer, KindType> kindMap = new HashMap<>();
			kindMap.put(0, KindType.SPENDING);
			kindMap.put(1, KindType.INCOME);
			kindMap.put(2, KindType.TRANSFER);

			Map<Integer, CategoryVo> parentMap = new HashMap<>();
			while (rs.next()) {
				int key = rs.getInt("it_key");
				int highKey = rs.getInt("it_highkey");
				int isincome = rs.getInt("it_isincome");
				int order = rs.getInt("it_order");
				String name = rs.getString("it_name");

				CategoryVo category = new CategoryVo();
				category.setKind(kindMap.get(isincome));
				category.setName(name);
				category.setOrderNo(order);

				if (highKey == -1) {
					category.setParentSeq(0);
				} else {
					CategoryVo parent = parentMap.get(highKey);
					category.setParentSeq(parent.getCategorySeq());
				}
				categoryRepository.saveAndFlush(category);
				parentMap.put(key, category);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
