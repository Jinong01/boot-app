
//매개변수를 갯수 제한 없이 여러개 받을 수 있음
//...(스프레드 또는 레스트동작) 문법
//1. 스프레드 > 구조를 분해
//2. 레스트 > 열거형태 n개의 매개변수를 갖는다.
//여기선 2번으롤 사용
function makeTr (...tdNodes) {
    const tr = document.createElement('tr');
    tdNodes.forEach(td => {
        tr.append(td);
    })

    return tr;
}

function makeTd(contents, className) {
    const td = document.createElement('td');
    if (typeof contents === 'string'){
        td.innerHTML = contents;
    }  else if (typeof contents === 'object'){
        td.append(contents)
    }

    //클래스 이름이 넘어올 경우 등록
    if (typeof className === 'string' && className.trim().length > 0){
        td.classList.add(className);
    }
    return td;
}

function makeCheckbox(id, name, value, className){
    const checkBox = document.createElement('input');
    checkBox.type = 'checkbox';
    checkBox.id = `check_${id}`;
    checkBox.name = name;
    checkBox.value = value;

    //클래스 이름이 넘어올 경우 등록
    if (typeof className === 'string' && className.trim().length > 0){
        checkBox.classList.add(className);
    }
    return checkBox;
}