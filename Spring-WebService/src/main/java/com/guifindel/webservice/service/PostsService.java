package com.guifindel.webservice.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.guifindel.webservice.controller.PostsSaveRequestDto;
import com.guifindel.webservice.domain.PostsRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostsService {

	private PostsRepository postsRepository;
	
	@Transactional
	public Long save(PostsSaveRequestDto dto) {
		return postsRepository.save(dto.toEntity()).getId();
	}
	
	/**
	 * 호출한 쪽에서 저장한 게시글의 id를 알 수 있도록 리턴 타입을 Long로 두고, .getId()를 반환값으로 지정한다.
	 * Service 메소드는 Entity를 바로 받지 않고, 이전에 생성한 save용 DTO인 PostsSaveRequestDTO를 받아서 저장한다.
	 * 
	 * Controller에서 Dto.toEntity를 통해서 바로 전달해도 되는데 굳이 Service에서 Dto를 받은 이유가 있다.
	 * Controller와 Service의 역할을 분리하기 위함이 그 이유이다.
	 * 비즈니스 로직*트랜잭션 관리는 모두 Service에서 관리하고, View와 연동되는 부분은 Controller에서 담당하도록 구성한다.
	 * 
	 * 트랜잭션이란?
	 * 일반적으로 DB데이터를 등록/수정/삭제 하는 Service 메소드는 @Transactional를 필수적으로 가져간다.
	 * 이 어노테이션이 하는일은 간단한데, 메소드 내에서 Exception이 발생하면 해당 메소드에서 이루어진 모든 DB작업을 초기화 시킨다.
	 * 즉, save 메소드를 통해서 10개를 드옭해야하는데 5번째에서 Exception이 발생하면 앞에 저장된 4개까지 전부 롤백을 한다.
	 * (좀 더 정확히 얘기한다면, 이미 넣은걸 롤배기키는 것이 아니며, 모든 처리가 정상적으로 됐을 대만 DB에 커밋하며 그렇지 않은 경우
	 * 에는 커밋하지 않는 것이다.)
	 */
}
