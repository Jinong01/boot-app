package kor.it.academy.board.mapper.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kor.it.academy.board.entity.BoardEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BoardSpecification implements Specification<BoardEntity> {

    private final String searchType;
    private final String searchText;

    public BoardSpecification(String searchType, String searchText){
        this.searchType = searchType;
        this.searchText = searchText;
    }

    @Override
    public Predicate toPredicate(Root<BoardEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchText == null || searchText.isBlank()){
            return null;
        }

        String likeText = "%" + searchText + "%";
        Predicate titleLike = cb.like(root.get("title"), likeText);
        Predicate writerLike = cb.like(root.get("writer"), likeText);

        if (searchType.equalsIgnoreCase("all")){
            predicates.add(cb.or(titleLike, writerLike));
        } else if (searchType.equalsIgnoreCase("title")) {
            predicates.add(titleLike);
        } else if (searchType.equalsIgnoreCase("writer")) {
            predicates.add(writerLike);
        }

        //list 를 배열타입으로 변경 해서 cb.and 함수 매개변수로 준 것
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
