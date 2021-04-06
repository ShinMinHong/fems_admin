package framework.spring.web.rest.jsonview;

import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import framework.base.model.AbleView;

/**
 * {@link Page}의 구현체인 {@link PageImpl}이 
 * {@link JsonView} Annotaion을 처리할 수 없어서 옮겨 담기 위한 {@link Page} 구현체
 * 
 * @author ByeongDon
 */
public class AblePageImpl<T> implements Page<T>, Slice<T> {

	@JsonIgnore
	private Page<T> page;
	
	public AblePageImpl(Page<T> page) {
		this.page = page;
	}
	
	public AblePageImpl(List<T> content, Pageable pageable, long total) {
		this.page = new PageImpl<>(content, pageable, total);
	}

	@Override
	public Iterator<T> iterator() {
		return page.iterator();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public int getNumber() {
		return page.getNumber();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public int getSize() {
		return page.getSize();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public int getNumberOfElements() {
		return page.getNumberOfElements();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public List<T> getContent() {
		return page.getContent();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public boolean hasContent() {
		return page.hasContent();
	}

	@JsonView(AbleView.CommonBaseView.class)
	public Sort getSort() {
		Sort ableSort = AbleSort.buildFrom(page.getSort());
		return ableSort;
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public boolean isFirst() {
		return page.isFirst();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public boolean isLast() {
		return page.isLast();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public boolean hasNext() {
		return page.hasNext();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public boolean hasPrevious() {
		return page.hasPrevious();
	}

	@Override
	public Pageable nextPageable() {
		return page.nextPageable();
	}

	@Override
	public Pageable previousPageable() {
		return page.previousPageable();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public int getTotalPages() {
		return page.getTotalPages();
	}

	@Override
	@JsonView(AbleView.CommonBaseView.class)
	public long getTotalElements() {
		return page.getTotalElements();
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		return page.map(converter);
	} 
	
}
