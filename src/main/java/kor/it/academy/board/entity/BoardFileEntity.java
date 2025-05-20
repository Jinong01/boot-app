package kor.it.academy.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "board_files")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;

    @Column(name = "stored_file_name", nullable = false, length = 100)
    private String storedFileName;

    @Column(name = "file_path", nullable = false, length = 200)
    private String filePath;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_seq", nullable = false)
    private BoardEntity board;
}