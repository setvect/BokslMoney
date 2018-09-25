package com.setvect.bokslmoney.util;

import java.util.ArrayList;

/**
 * 사이즈가 제한된 큐. <br>
 * 입력데이터가 정해진 사이즈를 초과하면 마지막에 입력 순으로 지움.
 *
 * @param <K>
 *            Type
 */
public class LimitedSizeQueue<K> extends ArrayList<K> {
	/** */
	private static final long serialVersionUID = -2788947967085061954L;
	/** 최대 사이즈 */
	private int maxSize;

	/**
	 * @param size
	 *            최대 저장 사이즈
	 */
	public LimitedSizeQueue(final int size) {
		this.maxSize = size;
	}

	@Override
	public boolean add(final K k) {
		boolean r = super.add(k);
		if (size() > maxSize) {
			removeRange(0, size() - maxSize - 1);
		}
		return r;
	}
}
