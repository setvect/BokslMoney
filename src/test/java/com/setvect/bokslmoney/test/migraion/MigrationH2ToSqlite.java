package com.setvect.bokslmoney.test.migraion;

import com.setvect.bokslmoney.account.repository.AccountRepository;
import com.setvect.bokslmoney.account.vo.AccountVo;
import com.setvect.bokslmoney.code.repository.CodeItemRepository;
import com.setvect.bokslmoney.code.repository.CodeMainRepository;
import com.setvect.bokslmoney.code.vo.CodeItemVo;
import com.setvect.bokslmoney.code.vo.CodeMainVo;
import com.setvect.bokslmoney.memo.repository.MemoRepository;
import com.setvect.bokslmoney.memo.vo.MemoVo;
import com.setvect.bokslmoney.test.MainTestBase;
import com.setvect.bokslmoney.transaction.repository.CategoryRepository;
import com.setvect.bokslmoney.transaction.repository.OftenUsedRepository;
import com.setvect.bokslmoney.transaction.repository.TransactionRepository;
import com.setvect.bokslmoney.transaction.vo.CategoryVo;
import com.setvect.bokslmoney.transaction.vo.OftenUsedVo;
import com.setvect.bokslmoney.transaction.vo.TransactionVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class MigrationH2ToSqlite extends MainTestBase {
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


    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Connection connSqlite = getSqliteDbConnect();

        migrationAccount(connSqlite);
        migrationCategory(connSqlite);
        migrationOftenUsed(connSqlite);
        migrationMemo(connSqlite);
        migrationTransaction(connSqlite);
        migrationCodeMain(connSqlite);
        migrationCodeItem(connSqlite);


        System.out.println("끝.");
    }

    private void migrationCodeItem(Connection connSqlite) throws SQLException {
        List<CodeItemVo> allList = codeItemRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from CB_CODE_ITEM");
        ps.executeUpdate();
        ps.close();
        ps = connSqlite.prepareStatement("insert into CB_CODE_ITEM('code_main_id','code_item_seq', 'name', 'order_no', 'delete_f') values (?,?,?,?,?) ");

        int count = 0;
        for (CodeItemVo item : allList) {
            ps.setString(1, item.getCodeItemKey().getCodeMain().getCodeMainId());
            ps.setInt(2, item.getCodeItemKey().getCodeItemSeq());
            ps.setString(3, item.getName());
            ps.setInt(4, item.getOrderNo());
            ps.setInt(5, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("CB_CODE_ITEM %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();
        System.out.printf("CB_CODE_ITEM end. total: %,d\n", allList.size());
    }

    private void migrationCodeMain(Connection connSqlite) throws SQLException {
        List<CodeMainVo> allList = codeMainRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from CA_CODE_MAIN");
        ps.executeUpdate();
        ps.close();
        ps = connSqlite.prepareStatement("insert into CA_CODE_MAIN('code_main_id', 'name', 'delete_f') values (?,?,?) ");

        int count = 0;
        for (CodeMainVo item : allList) {
            ps.setString(1, item.getCodeMainId());
            ps.setString(2, item.getName());
            ps.setInt(3, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("CA_CODE_MAIN %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();
        System.out.printf("CA_CODE_MAIN end. total: %,d\n", allList.size());
    }

    private void migrationTransaction(Connection connSqlite) throws SQLException {
        List<TransactionVo> allList = transactionRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from BE_TRANSACTION");
        ps.executeUpdate();
        ps.close();

        ps = connSqlite.prepareStatement("insert into BE_TRANSACTION('transaction_seq','category_seq','kind','pay_account','receive_account','attribute','money','transaction_date','note','fee') values (?,?,?,?,?, ?,?,?,?,?) ");

        int count = 0;
        for (TransactionVo item : allList) {
            ps.setInt(1, item.getTransactionSeq());
            ps.setInt(2, item.getCategory().getCategorySeq());
            ps.setString(3, item.getKind().name());
            ps.setInt(4, item.getPayAccount());
            ps.setInt(5, item.getReceiveAccount());
            ps.setInt(6, item.getAttribute());
            ps.setInt(7, item.getMoney());
            ps.setString(8, DateFormatUtils.format(item.getTransactionDate(), "yyyy-MM-dd HH:mm:ss.SSS"));
            ps.setString(9, item.getNote());
            ps.setInt(10, item.getFee());
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("BE_TRANSACTION %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();
        System.out.printf("BE_TRANSACTION end. total: %,d\n", allList.size());
    }

    private void migrationMemo(Connection connSqlite) throws SQLException {
        List<MemoVo> allList = memoRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from BD_MEMO");
        ps.executeUpdate();
        ps.close();
        ps = connSqlite.prepareStatement("insert into BD_MEMO('memo_seq', 'note', 'memo_date', 'delete_f') values (?,?,?,?) ");

        int count = 0;
        for (MemoVo item : allList) {
            ps.setInt(1, item.getMemoSeq());
            ps.setString(2, item.getNote());
            ps.setString(3, DateFormatUtils.format(item.getMemoDate(), "yyyy-MM-dd HH:mm:ss.SSS"));
            ps.setInt(4, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("BD_MEMO %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();
        System.out.printf("BD_MEMO end. total: %,d\n", allList.size());

    }

    private void migrationOftenUsed(Connection connSqlite) throws SQLException {
        List<OftenUsedVo> allList = oftenUsedRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from BC_OFTEN_USED");
        ps.executeUpdate();
        ps.close();
        ps = connSqlite.prepareStatement("insert into BC_OFTEN_USED('often_used_seq','category_seq','kind','title','pay_account','receive_account','money','note','attribute','order_no','delete_f') values (?,?,?,?,?, ?,?,?,?,?, ?) ");

        int count = 0;
        for (OftenUsedVo item : allList) {
            ps.setInt(1, item.getOftenUsedSeq());
            ps.setInt(2, item.getCategory().getCategorySeq());
            ps.setString(3, item.getKind().name());
            ps.setString(4, item.getTitle());
            ps.setInt(5, item.getPayAccount());
            ps.setInt(6, item.getReceiveAccount());
            ps.setInt(7, item.getMoney());
            ps.setString(8, item.getNote());
            ps.setInt(9, item.getAttribute());
            ps.setInt(10, item.getOrderNo());
            ps.setInt(11, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("BC_OFTEN_USED %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();
        System.out.printf("BC_OFTEN_USED end. total: %,d\n", allList.size());
    }

    private void migrationCategory(Connection connSqlite) throws SQLException {
        List<CategoryVo> allList = categoryRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from BB_CATEGORY");
        ps.executeUpdate();
        ps.close();

        ps = connSqlite.prepareStatement("insert into BB_CATEGORY('category_seq', 'kind', 'name', 'parent_seq', 'order_no', 'delete_f') values (?,?,?,?,?, ?) ");

        int count = 0;
        for (CategoryVo item : allList) {
            ps.setInt(1, item.getCategorySeq());
            ps.setString(2, item.getKind().name());
            ps.setString(3, item.getName());
            ps.setInt(4, item.getParentSeq());
            ps.setInt(5, item.getOrderNo());
            ps.setInt(6, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();

            count++;
            if (count % 10 == 0) {
                System.out.printf("BB_CATEGORY %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();

        System.out.printf("BB_CATEGORY end. total: %,d\n", allList.size());

    }

    private void migrationAccount(Connection connSqlite) throws SQLException {
        List<AccountVo> allList = accountRepository.findAll();
        PreparedStatement ps = connSqlite.prepareStatement("delete from BA_ACCOUNT");
        ps.executeUpdate();
        ps.close();

        ps = connSqlite.prepareStatement("insert into BA_ACCOUNT ('account_seq', 'name', 'account_number', 'kind_code', 'balance', 'interest_rate', 'term', 'exp_date', 'monthly_pay', 'transfer_date', 'note', 'delete_f') values (?, ?, ?, ?, ?,    ?, ?, ?, ?, ?,    ?, ?)");

        int count = 0;
        for (AccountVo item : allList) {
            ps.setInt(1, item.getAccountSeq());
            ps.setString(2, item.getName());
            ps.setString(3, StringUtils.defaultString(item.getAccountNumber(), "empty"));
            ps.setInt(4, item.getKindCode());
            ps.setInt(5, item.getBalance());
            ps.setString(6, item.getInterestRate());
            ps.setString(7, item.getTerm());
            ps.setString(8, item.getExpDate());
            ps.setString(9, item.getMonthlyPay());
            ps.setString(10, item.getTransferDate());
            ps.setString(11, item.getNote());
            ps.setInt(12, item.isDeleteFlag() ? 1 : 0);
            ps.executeUpdate();
            count++;
            if (count % 10 == 0) {
                System.out.printf("BA_ACCOUNT %,d/%,d\n", count, allList.size());
            }
        }
        ps.close();

        System.out.printf("BA_ACCOUNT end. total: %,d\n", allList.size());
    }


    public static Connection getSqliteDbConnect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:D:/intellij-project/BokslMoneyApp/db/bokslmoney.db";

        Connection conn = DriverManager.getConnection(url);
        return conn;
    }
}
