package com.setvect.bokslmoney.test.household;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.setvect.bokslmoney.memo.controller.MemoSearchParam;
import com.setvect.bokslmoney.memo.repository.MemoRepository;
import com.setvect.bokslmoney.memo.vo.MemoVo;
import com.setvect.bokslmoney.test.MainTestBase;
import com.setvect.bokslmoney.util.DateUtil;
import com.setvect.bokslmoney.util.PageResult;

public class MemoTestCase extends MainTestBase {

	@Autowired
	private MemoRepository memoRepository;

	@Test
	public void testAdd() {
		LocalDate localDate = LocalDate.of(2018, 5, 1);

		for (int i = 1; i <= 100; i++) {
			Date date = DateUtil.toDate(localDate);
			MemoVo memo = new MemoVo();
			memo.setMemoDate(date);
			memo.setNote("contents " + i);
			memoRepository.save(memo);
			localDate = localDate.plusDays(1);
		}

		MemoSearchParam searchCondition = new MemoSearchParam(0, 10);
		Date from = DateUtil.toDate(LocalDate.of(2018, 5, 1));
		Date to = DateUtil.toDate(LocalDate.of(2018, 5, 3));
		searchCondition.setFrom(from);
		searchCondition.setTo(to);

		PageResult<MemoVo> a = memoRepository.getPagingList(searchCondition);
		a.getList().stream().forEach(m -> {
			System.out.println(m);
		});

		System.out.println(a.getTotalCount());
	}

}
