var main = {
	init : function() {
		var _this = this;
		$('#btn-save').on('click', function() {
			_this.save();
		});
	},
	save : function() {
		var data = {
			title : $('#title').val(),
			author : $('#author').val(),
			content : $('#content').val()
		};

		$.ajax({
			type : 'POST',
			url : '/posts',
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(data)
		}).done(function() {
			alert('글이 등록되었습니다.');
			location.reload();
		}).fail(function(error) {
			alert(error);
		});
	}
};

main.init();

/**
 * js 첫 문장에 var main = {...}를 선언한 이유에 대해서 굳이 main이라는 변수의 속성으로 function을 추가한 이유는
 * main.js가 아래와같이 function을 작성한 상황에서
 * 
 * var init = function() { .... };
 * 
 * var save = function() { .... };
 * 
 * init();
 * 
 * 
 * main.hbs에서 a.js가 추가 된다고 가정했을 때 여기서 a.js가 init, save function이 없을 때는 괜찮겠으나
 * a.js가 init function과 save function이 있다고 가정 할 때(같은 명칭의 함수를 가진 서로 다른 js가 하나의
 * html에 적용됐을 때) 문제가 발생한다.
 * 
 * 브라우저의 scope는 공용으로 쓰이기 때문에 나중에 불려진 js의 init, save가 먼저 불려진 js의 function을 덮어쓴다.
 * 
 * 이런 문제를 피하려고 main.js만의 변수, function 영역으로 var main 이란 객체 안에서 function을 선언한다.
 * 이렇게 되면 main 객체 안에서 function을 선언한다 이렇게 되면 main 객체 안에서만 이름이 유효하기 때문에 다른 js와 겹칠
 * 위험이 사라진다.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
