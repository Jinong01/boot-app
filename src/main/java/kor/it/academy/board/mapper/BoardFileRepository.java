package kor.it.academy.board.mapper;

import kor.it.academy.board.entity.BoardEntity;
import kor.it.academy.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Integer> {
}
