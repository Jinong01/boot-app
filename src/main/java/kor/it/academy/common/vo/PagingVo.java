package kor.it.academy.common.vo;

import org.springframework.stereotype.Component;

@Component
public class PagingVo {

    private int totalPage; // 전체 페이지 개수
    private int totalRows; // 전체 데이터 개수
    private int pagePerRows; // 한 페이지 보여줄 데이터 개수
    private int currentPage; // 현재 페이지

    private int totalBlock; // 전체 블록 개수
    private int nowBlock; // 현재 블록 위치
    private int blockSize; // 한 블록에 보여줄 페이지 개수

    public void setData(int currentPage, int totalRows){
        this.currentPage = currentPage;
        this.totalRows = totalRows;
        this.pagePerRows = 10;
        this.blockSize = 10;
    }

    public int getOffSet(){
        return (this.currentPage * this.pagePerRows);
    }

    public int getCount(){
        return this.pagePerRows;
    }

    // 전체 페이지 수 계산
    private void getTotalPage(){
        double val = (double)this.totalRows/this.pagePerRows;
        this.totalPage = (int)Math.ceil(val);
    }

    // 현재 블록 계산
    private void getNowBlock(){
        double val = (double)this.currentPage/this.blockSize;
        this.nowBlock = (int)Math. floor(val);
    }

    // 전체 블록 계산
    private void getTotalBlock(){
        double val = (double)this.totalBlock/this.blockSize;
        this.totalBlock = (int)Math.ceil(val);
    }

    //페이지 html 처리
    public String pageHtml(){
        StringBuilder html = new StringBuilder();

        this.getTotalPage();
        this.getTotalBlock();
        this.getNowBlock();

        int pageNum = 0;
        String disabled = "disabled";
        String actiive = "";

        if(this.currentPage > 0){
            disabled = "";
        }

        //처음으로 가기 만들기
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(0);'>처음</a>");
        html.append("</li>");

        //이전(전 블록으로 이동) 만들기
        disabled = "disabled";
        if(this.nowBlock > 0) {
            disabled = "";
        }

        pageNum = (this.nowBlock * this.blockSize) -1;
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage("+ pageNum +");'>이전</a>");
        html.append("</li>");

        //페이지 번호 리스트 그리기
        for(int i = 0; i < this.blockSize; i++) {
            pageNum = (this.nowBlock * this.blockSize) + i;

            actiive = "";
            if (pageNum == this.currentPage) {
                actiive = "active";
            }

            html.append("<li class='page-item " + actiive + "'>");
            html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(" + pageNum + ");'>");
            html.append((pageNum + 1) + "</a></li>");

            //전체페이지가 10페이지가 안될경우 중간에 끊는다
            if(this.totalPage == 0 || this.totalPage == (pageNum + 1)){
                break;
            }
        }

        //다음(다음 블록으로 이동) 만들기
        disabled = "disabled";
        if((this.currentPage + 1) < this.totalBlock) {
            disabled = "";
        }

        pageNum = ((this.nowBlock + 1) * this.blockSize);
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage("+ pageNum +");'>다음</a>");
        html.append("</li>");

        //마지막 페이지 가기
        disabled = "disabled";
        if(this.totalPage > (this.currentPage + 1)){
            disabled = "";
        }

        pageNum = this.totalPage - 1;

//        String item = """
//                <li class='page-item %s'>
//                <a class='page-link' href='javascript:void(0);' onclick='movePage(%d);'>%s</a></li>
//                """.formatted(disabled, pageNum, "마지막");

        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage("+ pageNum +");'>마지막</a>");
        html.append("</li>");


        return html.toString();
    }
}
