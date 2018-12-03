package com.setvect.bokslmoney.migration.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.hab.repository.AccountRepository;
import com.setvect.bokslmoney.hab.vo.AccountVo;

@Service
public class MigrationService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CodeItemRepository codeItemRepository;

	@Autowired
	private CodeMainRepository codeMainRepository;

	public void run() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		CodeMainVo kindCode = codeMainRepository.findById(CodeKind.KIND_CODE.name()).get();

		try {
			String url = "jdbc:sqlite:./temp/setvect.db";
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
		System.out.println("끝.");
	}
}
