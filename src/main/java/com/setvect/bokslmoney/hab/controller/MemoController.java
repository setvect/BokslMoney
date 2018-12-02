package com.setvect.bokslmoney.hab.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.setvect.bokslmoney.ApplicationUtil;
import com.setvect.bokslmoney.hab.repository.MemoRepository;
import com.setvect.bokslmoney.hab.vo.MemoVo;
import com.setvect.bokslmoney.util.DateUtil;
import com.setvect.bokslmoney.util.PageResult;

/**
 * 메모 관리
 */
@Controller
@RequestMapping(value = "/hab/memo/")
public class MemoController {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(MemoController.class);

	@Autowired
	private MemoRepository memoRepository;

	// ============== 뷰==============

	// ============== 조회 ==============

	/**
	 * @param year
	 *            년
	 * @param month
	 *            월
	 * @return 해당 월에 메모
	 */
	@RequestMapping(value = "/listByMonth.json")
	@ResponseBody
	public ResponseEntity<String> listByMonth(final int year, final int month) {
		MemoSearchParam searchCondition = new MemoSearchParam(0, Integer.MAX_VALUE);
		LocalDate fromDate = LocalDate.of(year, month, 1);
		LocalDate toDate = fromDate.plusMonths(1).minusDays(1);

		searchCondition.setFrom(DateUtil.toDate(fromDate));
		searchCondition.setTo(DateUtil.toDate(toDate));

		PageResult<MemoVo> page = memoRepository.getPagingList(searchCondition);
		List<MemoVo> list = page.getList();
		String json = ApplicationUtil.toJson(list, "**,item[-handler,-hibernateLazyInitializer]");
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	// ============== 등록 ==============
	@RequestMapping(value = "/add.do")
	@ResponseBody
	public void add(final MemoVo item) {
		memoRepository.save(item);
	}

	// ============== 수정 ==============
	@RequestMapping(value = "/edit.do")
	@ResponseBody
	public void edit(final MemoVo item) {
		memoRepository.save(item);
	}

	// ============== 삭제 ==============
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public void delete(@RequestParam("memoSeq") final int itemSeq) {
		MemoVo item = memoRepository.findById(itemSeq).get();
		item.setDeleteFlag(true);
		memoRepository.save(item);
	}
}
