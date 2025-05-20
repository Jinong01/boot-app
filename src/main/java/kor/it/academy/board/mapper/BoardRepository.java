package kor.it.academy.board.mapper;

import kor.it.academy.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>,
                                            JpaSpecificationExecutor<BoardEntity> {
}
