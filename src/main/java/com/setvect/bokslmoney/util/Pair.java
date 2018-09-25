package com.setvect.bokslmoney.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 두 쌍의 값 표현
 *
 * @param <L>
 *            왼쪽 데이터 타입
 * @param <R>
 *            오른쪽 데이터 타입
 */
public class Pair<L, R> {
	/** 왼쪽 값 */
	private final L left;
	/** 오른쪽 값 */
	private final R right;

	/**
	 * @param left
	 *            왼쪽 값
	 * @param right
	 *            오른쪽 값
	 */
	public Pair(final L left, final R right) {
		super();
		this.left = left;
		this.right = right;
	}

	/**
	 * @return 왼쪽 값
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * @return 오른쪽 값
	 */
	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
