package com.setvect.bokslmoney.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Tree 자료구조<br>
 * 예) 디렉토리, 분류 카테고리
 *
 * @param <T> Tree 항목
 */
public class TreeNode<T> implements Iterable<TreeNode<T>> {
	/** 로깅 */
	private static Logger logger = LoggerFactory.getLogger(TreeNode.class);

	/** 최대 탬색 dpeth. 해당 값을 넘어가면 예외 발생 */
	private static final int MAX_DEPTH = 50;
	/** 항목 */
	private T data;
	/** 부모 항목 */
	@JsonIgnore
	private TreeNode<T> parent;
	/** 자식 항목 */
	private List<TreeNode<T>> children;

	/**
	 * 최초 루트 만들 때
	 *
	 * @param data 최상의 루트 항목
	 */
	public TreeNode(final T data) {
		this.data = data;
		this.children = new ArrayList<>();
	}

	/**
	 * 자식 항목 추가
	 *
	 * @param child 자식 항목
	 * @return 자식 노드
	 */
	public TreeNode<T> addChild(final T child) {
		TreeNode<T> childNode = new TreeNode<>(child);
		return addChild(childNode);
	}

	/**
	 * 자식 항목 추가
	 *
	 * @param childNode 자식 항목
	 * @return 자식 노드
	 */
	public TreeNode<T> addChild(final TreeNode<T> childNode) {
		if (isCircularReference(childNode)) {
			logger.warn("Circular reference error. {}", childNode.getData().toString());
			return null;
		}

		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	/**
	 * @param childNode 입력할 노드
	 * @return 입력할 노드가 순환참조이면 true, 아니면 false
	 */
	private boolean isCircularReference(final TreeNode<T> childNode) {
		long exist = this.getPathNode().stream().filter(node -> node == childNode).count();
		return exist != 0;
	}

	/**
	 * @return 자식 노드
	 */
	public List<TreeNode<T>> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * @return 부모 노드
	 */
	public TreeNode<T> getParent() {
		return parent;
	}

	/**
	 * @return 노드 값
	 */
	public T getData() {
		return data;
	}

	/**
	 * 트리 레벨. 0부터 시작
	 *
	 * @return 트리 레벨. 0부터 시작
	 */
	@JsonIgnore
	public int getLevel() {
		return getPath().size() - 1;
	}

	/**
	 * 루트 카테고리리 부터 현재 카테고리까지 경로를 구한다.
	 *
	 * @return 카테고리 경로<br>
	 *         root > depth1 > depth2 ...
	 */
	@JsonIgnore
	private List<TreeNode<T>> getPathNode() {
		List<TreeNode<T>> result = new ArrayList<>();

		TreeNode<T> current = this;
		result.add(current);

		for (int i = 0; i < MAX_DEPTH && !current.isRoot(); i++) {
			current = current.parent;
			result.add(current);
		}
		Collections.reverse(result);
		return result;
	}

	/**
	 * 루트 카테고리리 부터 현재 카테고리까지 경로를 구한다.
	 *
	 * @return 카테고리 경로<br>
	 *         root > depth1 > depth2 ...
	 */
	@JsonIgnore
	public List<T> getPath() {
		List<TreeNode<T>> node = getPathNode();
		return node.stream().map(n -> n.data).collect(Collectors.toList());
	}

	/**
	 * @return 최상단 자료이면 true, 아니면 false
	 */
	@JsonIgnore
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * 해당 값의 서브 로드를 반환
	 *
	 * @param data 찾기 위한 값
	 * @return 노드 값에 서브 노드
	 */
	public TreeNode<T> getTreeNode(final T data) {
		List<TreeNode<T>> list = exploreTree();
		Optional<TreeNode<T>> result = list.stream().filter(p -> p.data.equals(data)).findAny();
		return result.orElse(null);
	}

	/**
	 * 전위 순회 방식으로 제공
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<TreeNode<T>> iterator() {
		List<TreeNode<T>> nodeList = exploreTree();
		return nodeList.iterator();
	}

	/**
	 * @return 카테고리 트리 형태로 구성된 문자열
	 */
	public String printData() {
		StringBuffer result = new StringBuffer();
		Iterator<TreeNode<T>> iter = iterator();
		while (iter.hasNext()) {
			TreeNode<T> next = iter.next();
			String depthPadding = String.join("", Collections.nCopies(next.getLevel(), "--"));
			result.append(depthPadding + next.getData() + "\n");
		}
		return result.toString();
	}

	/**
	 * 트리 전체를 탐색
	 *
	 * @return 트리 전체를 탐색
	 */
	public List<TreeNode<T>> exploreTree() {
		TreeNode<T> currentNode = TreeNode.this;
		List<TreeNode<T>> nodeList = new ArrayList<>();
		addNode(currentNode, nodeList, 0);
		return nodeList;
	}

	/**
	 * @param node     추가할 노드
	 * @param nodeList 추가 대상 노드 목록
	 * @param depth    현재 탐색 깊이
	 */
	private void addNode(final TreeNode<T> node, final List<TreeNode<T>> nodeList, final int depth) {
		if (depth >= MAX_DEPTH) {
			List<T> pathList = node.getPath();
			String path = StringUtils.join(pathList, " > ");

			String message = String.format("The maximum depth %d has been exceeded. path: %s", MAX_DEPTH, path);
			logger.warn(message);
			return;
		}
		nodeList.add(node);
		node.children.stream().forEach(p -> addNode(p, nodeList, depth + 1));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
