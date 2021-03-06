package com.setvect.bokslmoney.test.migraion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.service.CodeKind;
import com.setvect.bokslmoney.code.vo.CodeItemKey;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.memo.repository.MemoRepository;
import com.setvect.bokslmoney.memo.vo.MemoVo;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.repository.OftenUsedRepository;
import com.setvect.bokslmoney.transaction.repository.TransactionRepository;
import com.setvect.bokslmoney.transaction.vo.CategoryVo;
import com.setvect.bokslmoney.transaction.vo.KindType;
import com.setvect.bokslmoney.transaction.vo.OftenUsedVo;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import com.setvect.bokslmoney.util.DateUtil;

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

	@Autowired
	private OftenUsedRepository oftenUsedRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private MemoRepository memoRepository;

	// 기존 정보와 새롭게 입력한 정보 맵핑
	/** Key: 기존 계좌 Key, 새롭게 입력한 계좌 */
	Map<Integer, AccountVo> accountVoMap = new HashMap<>();

	/** Key: 기존분류 Key, 새롭게 입력한 분류 */
	Map<Integer, CategoryVo> categoryVoMap = new HashMap<>();

	/** 찾지 못한 분류(삭제된 분류라는 뜻) */
	Map<KindType, CategoryVo> categoryVoMapNotFound = new HashMap<>();

	/** 삭제된 계좌 유형 */
	private static final int DELETE_KIND_CODE = 1000;

	static Map<Integer, KindType> kindMap = new HashMap<>();
	static {
		kindMap.put(0, KindType.SPENDING);
		kindMap.put(1, KindType.INCOME);
		kindMap.put(2, KindType.TRANSFER);
	}

	public void run() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Map<String, CodeMainVo> codeMainMap = Stream.of(CodeKind.values()).map(code -> {
			CodeMainVo codeMainVo = codeMainRepository.findById(code.name()).get();
			return codeMainVo;
		}).collect(Collectors.toMap(code -> code.getCodeMainId(), Function.identity()));

		try {
			String url = "jdbc:sqlite:./temp/setvect.db";

			// 0. 코드기본 입력
			saveCodeItem(codeMainMap.get(CodeKind.ATTR_INCOME.name()), 0, "단순 수입", 1);
			saveCodeItem(codeMainMap.get(CodeKind.ATTR_INCOME.name()), 1, "투자 수입", 2);

			saveCodeItem(codeMainMap.get(CodeKind.ATTR_SPENDING.name()), 2, "단순 지출", 1);
			saveCodeItem(codeMainMap.get(CodeKind.ATTR_SPENDING.name()), 7, "고정 지출", 2);

			saveCodeItem(codeMainMap.get(CodeKind.ATTR_TRANSFER.name()), 5, "단순 이체", 1);
			saveCodeItem(codeMainMap.get(CodeKind.ATTR_TRANSFER.name()), 6, "투자 이체", 2);
			saveCodeItem(codeMainMap.get(CodeKind.ATTR_TRANSFER.name()), 11, "부채 이체", 3);

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
				codeItem.setCodeItemKey(new CodeItemKey(codeMainMap.get(CodeKind.KIND_CODE.name()), key));
				codeItem.setName(name);
				codeItem.setOrderNo(order);
				codeItemRepository.saveAndFlush(codeItem);
			}

			CodeItemVo codeItem = new CodeItemVo();
			codeItem.setCodeItemKey(new CodeItemKey(codeMainMap.get(CodeKind.KIND_CODE.name()), DELETE_KIND_CODE));
			codeItem.setName("삭제 자산");
			codeItem.setOrderNo(99);
			codeItemRepository.saveAndFlush(codeItem);

			rs.close();
			ps.close();

			// 2. 계좌 정보
			ps = conn.prepareStatement("select  * from t_account");
			rs = ps.executeQuery();
			// accountRepository.deleteAll();
			while (rs.next()) {
				AccountVo account = new AccountVo();
				int key = rs.getInt("a_key");
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

				putAccountVoMap(key, account);
			}
			rs.close();
			ps.close();

			// 3. 항목 코드
			ps = conn.prepareStatement("select  * from t_item order by it_key");
			rs = ps.executeQuery();

			while (rs.next()) {
				int key = rs.getInt("it_key");
				int highKey = rs.getInt("it_highkey");
				int isincome = rs.getInt("it_isincome");
				int order = rs.getInt("it_order");
				String name = rs.getString("it_name");

				if (key == 17) {
					System.out.println();
				}
				CategoryVo category = new CategoryVo();
				category.setKind(getKindType(isincome));
				category.setName(name);
				category.setOrderNo(order);

				if (highKey == -1) {
					category.setParentSeq(0);
				} else {
					category.setParentSeq(getCategoryVoMap(highKey).getCategorySeq());
				}
				categoryRepository.saveAndFlush(category);
				putCategoryVoMap(key, category);
			}

			Stream.of(KindType.values()).forEach(kind -> {
				CategoryVo categoryMain = new CategoryVo();
				categoryMain.setKind(kind);
				categoryMain.setName("삭제 분류(" + kind.name() + ", 대)");
				categoryMain.setOrderNo(1);
				categoryMain.setParentSeq(0);
				categoryRepository.saveAndFlush(categoryMain);

				CategoryVo categorySub = new CategoryVo();
				categorySub.setKind(kind);
				categorySub.setName("삭제 분류(" + kind.name() + ", 소)");
				categorySub.setOrderNo(1);
				categorySub.setParentSeq(categoryMain.getCategorySeq());
				categoryRepository.saveAndFlush(categorySub);

				categoryVoMapNotFound.put(kind, categorySub);
			});

			rs.close();
			ps.close();

			// 4. 자주 쓰는 거래
			ps = conn.prepareStatement("select * from t_inputs order by i_key");
			rs = ps.executeQuery();
			while (rs.next()) {
				// int key = rs.getInt("i_key"); // not used
				String datas = rs.getString("i_datas");
				int type = rs.getInt("i_type");
				int order = rs.getInt("i_order");

				String[] dataToken = datas.split("\n");
				String title = dataToken[0];
				String note = dataToken[1];
				int itemKey = Integer.parseInt(dataToken[2]);
				int money = (int) Double.parseDouble(dataToken[3]);
				money = money == -1 ? 0 : money;
				int receiveAccountKey = Integer.parseInt(dataToken[4]);
				int payAccountKey = Integer.parseInt(dataToken[5]);
				int attribute = Integer.parseInt(dataToken[6]);

				OftenUsedVo used = new OftenUsedVo();
				used.setCategory(getCategoryVoMap(itemKey));
				KindType kind = KindType.INCOME;
				if (type == 1) {
					kind = KindType.SPENDING;
				} else if (type == 2) {
					kind = KindType.TRANSFER;
				}

				used.setKind(kind);
				used.setTitle(title);
				used.setPayAccount(getAccountVoMapSeq(payAccountKey));
				used.setReceiveAccount(getAccountVoMapSeq(receiveAccountKey));
				used.setMoney(money);
				used.setNote(note);
				used.setAttribute(attribute);
				used.setOrderNo(order);
				oftenUsedRepository.saveAndFlush(used);
			}
			rs.close();
			ps.close();

			// 5. 메모
			ps = conn.prepareStatement("select * from t_memo order by pkey");
			rs = ps.executeQuery();

			while (rs.next()) {
				String dateStr = rs.getString("tdate");
				Date date = DateUtil.getDate(dateStr, "yyyy-MM-dd");
				String title = rs.getString("title");
				String memo = rs.getString("tmemo");

				MemoVo memoVo = new MemoVo();
				memoVo.setMemoDate(date);
				String note = title;
				if (StringUtils.isNotBlank(memo)) {
					note += "\n\n" + memo;
				}
				memoVo.setNote(note.trim());

				memoRepository.saveAndFlush(memoVo);
			}
			rs.close();
			ps.close();

			// 6. 거래 내역
			ps = conn.prepareStatement("select * from t_transfer order by t_key");
			rs = ps.executeQuery();
			while (rs.next()) {
				String dateStr = rs.getString("t_date");
				Date date = DateUtil.getDate(dateStr, "yyyy-MM-dd");
				int low = rs.getInt("t_low");
				String memo = rs.getString("t_memo");
				int from = rs.getInt("t_from");
				int to = rs.getInt("t_to");
				int money = rs.getInt("t_money");
				int type = rs.getInt("t_type");
				int ttype = rs.getInt("t_ttype");
				int fee = rs.getInt("t_fee");
				CategoryVo category = getCategoryVoMap(low);
				KindType kind = KindType.INCOME;
				if (type == 1) {
					kind = KindType.SPENDING;
				} else if (type == 2) {
					kind = KindType.TRANSFER;
				}

				// 기존에 없으면 삭제된 카테고리라는 뜻.
				if (category == null) {
					category = categoryVoMapNotFound.get(kind);
				}

				TransactionVo tran = new TransactionVo();
				tran.setCategory(category);
				tran.setKind(kind);
				tran.setPayAccount(getAccountVoMapSeq(from));
				tran.setReceiveAccount(getAccountVoMapSeq(to));
				tran.setAttribute(ttype);
				tran.setMoney(money);
				tran.setTransactionDate(date);
				tran.setNote(memo);
				tran.setFee(fee);

				transactionRepository.save(tran);
			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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

	private void putCategoryVoMap(int key, CategoryVo category) {
		categoryVoMap.put(key, category);
	}

	private CategoryVo getCategoryVoMap(int category) {
		CategoryVo categoryVo = categoryVoMap.get(category);
		return categoryVo;
	}

	private int getAccountVoMapSeq(int accountKey) {
		if (accountKey == -1) {
			return 0;
		}
		AccountVo ac = getAccountVoMap(accountKey);
		if (ac == null) {
			AccountVo acc = new AccountVo();
			acc.setName("삭제된 계좌 #" + accountKey);
			acc.setKindCode(DELETE_KIND_CODE);
			acc.setDeleteFlag(true);
			accountRepository.saveAndFlush(acc);
			putAccountVoMap(accountKey, acc);
			ac = acc;
		}
		return ac.getAccountSeq();
	}

	private AccountVo getAccountVoMap(int accountKey) {
		return accountVoMap.get(accountKey);
	}

	private void putAccountVoMap(int key, AccountVo account) {
		accountVoMap.put(key, account);
	}

	private void saveCodeItem(CodeMainVo codeMain, int codeItemSeq, String name, int orderNo) {
		CodeItemVo code = new CodeItemVo();
		code.setCodeItemKey(new CodeItemKey(codeMain, codeItemSeq));
		code.setName(name);
		code.setOrderNo(orderNo);
		codeItemRepository.saveAndFlush(code);
	}

	/**
	 * 거래 유형 정보
	 *
	 * @param kindNum
	 * @return 거래 유형 정보
	 */
	public KindType getKindType(int kindNum) {
		return kindMap.get(kindNum);

	}
}
