package article1be.board.service;

import article1be.amazonS3.service.AmazonS3Service;
import article1be.board.dto.BoardDTO;
import article1be.board.dto.PictureDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.dto.RequestPicture;
import article1be.board.entity.Board;
import article1be.board.entity.Picture;
import article1be.board.repository.BoardRepository;
import article1be.board.repository.PictureRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;

    @Autowired
    BoardService(
            BoardRepository boardRepository,
            PictureRepository pictureRepository,
            AmazonS3Service amazonS3Service
    ) {
        this.boardRepository = boardRepository;
        this.pictureRepository = pictureRepository;
        this.amazonS3Service = amazonS3Service;
    }

    // 게시글 목록 조회
    public List<BoardDTO> getBoardList() {

        List<BoardDTO> boardDTOList = boardRepository.findByBoardIsBlindFalse()
                .stream()
                .map(board -> BoardDTO.builder()
                        .boardSeq(board.getBoardSeq())
                        .userSeq(board.getUserSeq())
                        .boardTitle(board.getBoardTitle())
                        .boardContent(board.getBoardContent())
                        .regDate(board.getRegDate())
                        .upDate(board.getUpDate())
                        .delDate(board.getDelDate())
                        .boardIsBlind(board.getBoardIsBlind())
                        .boardIsNotice(board.getBoardIsNotice())
                        .build())
                .collect(Collectors.toList());

        return boardDTOList;
    }

    // 게시글 조회
    public BoardDTO getBoard(long boardSeq) {
        Optional<Board> optionalBoard = boardRepository.findById(boardSeq);

        if (optionalBoard.isPresent()) {

            Board board = optionalBoard.get();

            // 게시물 번호에 해당하는 사진들을 호출
            List<PictureDTO> pictureDTOList = pictureRepository.findByPictureBoardSeq(boardSeq)
                    .stream()
                    .map(picture -> PictureDTO.builder()
                            .pictureSeq(picture.getPictureSeq())
                            .pictureBoardSeq(picture.getPictureBoardSeq())
                            .pictureOriginName(picture.getPictureOriginName())
                            .pictureChangedName(picture.getPictureChangedName())
                            .pictureUrl(picture.getPictureUrl())
                            .pictureType(picture.getPictureType())
                            .regDate(picture.getRegDate())
                            .delDate(picture.getDelDate())
                            .pictureIsDelete(picture.isPictureIsDelete())
                            .build())
                    .collect(Collectors.toList());

            return BoardDTO.builder()
                    .boardSeq(board.getBoardSeq())
                    .userSeq(board.getUserSeq())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardPictureList(pictureDTOList)
                    .regDate(board.getRegDate())
                    .upDate(board.getUpDate())
                    .delDate(board.getDelDate())
                    .boardIsBlind(board.getBoardIsBlind())
                    .boardIsNotice(board.getBoardIsNotice())
                    .build();
        } else {
            return null;
        }
    }

    // 게시글 생성
    @Transactional
    public Board createBoard(RequestBoard newBoard) throws IOException {
        Board board = new Board().create(
                123L,           // 테스트 데이터(로그인 구현완료되면 수정할 예정)
                newBoard.getBoardTitle(),
                newBoard.getBoardContent()
        );

        // 1. DB(Board)에 데이터 저장
        Board result = boardRepository.save(board);


        for (MultipartFile image : newBoard.getImageList()) {
            // 2. Amazon S3 버킷에 이미지 저장
            AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

            // 3. DB(Picture)에 데이터 저장
            RequestPicture requestPicture = new RequestPicture();
            requestPicture.setPictureBoardSeq(board.getBoardSeq());
            requestPicture.setPictureOriginName(metaData.getOriginalFileName());
            requestPicture.setPictureChangedName(metaData.getChangeFileName());
            requestPicture.setPictureUrl(metaData.getUrl());
            requestPicture.setPictureType(metaData.getType());

            Picture picture = new Picture();
            picture.create(requestPicture);

            pictureRepository.save(picture);
        }

        return result;
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardSeq) {
        // 1. DB(BOARD)에 해당 게시글 삭제
        boardRepository.deleteById(boardSeq);

        // 2. DB(PICTURE)에 해당 게시글 번호로 된 모든 데이터 블라인드 처리
        List<Picture> pictureList = pictureRepository.findByPictureBoardSeq(boardSeq);

        // 각 Picture 객체의 블라인드 처리
        for (Picture picture : pictureList) {
            picture.setBlind();
            pictureRepository.save(picture); // 변경된 Picture 객체를 저장
        }
    }

    // 게시글 수정
    @Transactional
    public Board upDateBoard(Long boardSeq, RequestBoard modData) throws IOException {
        Optional<Board> optionalBoard = boardRepository.findById(boardSeq);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get(); // Board 객체를 꺼냄
            board.update(modData); // update 메소드 호출

            return boardRepository.save(board); // 변경사항 저장
        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다."); // 게시글이 없을 경우 예외 처리
        }
    }

}
