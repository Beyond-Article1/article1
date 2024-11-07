<script setup>

</script>

<template>
  <div id="wrap" class="wrap">

    <header class="header" role="banner">
      <div class="gnb_area">
        <h1 class="text" style="margin-top:30px; margin-left:30px; font-size: 40px">MY PAGE</h1>
      </div>

      <div id="headerLeft" class="header_left">
        <ul class="left_menu" role="menu">
          <!-- router-link를 사용하여 라우팅을 관리 -->
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/detail" class="menu_text" exact-active-class="active-link">회원정보</router-link>
          </li>
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/preference" class="menu_text" exact-active-class="active-link">선호도</router-link>
          </li>
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/outfit/list" class="menu_text" exact-active-class="active-link">아웃핏 이력조회</router-link>
          </li>
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/outfit/stats" class="menu_text" exact-active-class="active-link">아웃핏 통계</router-link>
          </li>
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/review" class="menu_text" exact-active-class="active-link">리뷰 조회</router-link>
          </li>
          <li class="left_item" role="menuitem">
            <router-link to="/mypage/post" class="menu_text" exact-active-class="active-link">게시글 조회</router-link>
          </li>
        </ul>
      </div>
    </header>

    <div id="container" class="container mypage-content">
      <router-view />
    </div>

  </div>
</template>

<style scoped>
.wrap {
  display: flex;
  flex-direction: row;  /* 가로로 배치 */
  height: 100vh;        /* 전체 화면 높이를 채움 */
}

.header {
  position: relative;
  width: 395px;
  background-color: rgba(255, 255, 255, 0.7); /* 배경 투명하게 */
  box-shadow: 5px 1px 8px 0 rgba(0, 0, 0, .06);
  border-right: 1px solid rgba(0, 0, 0, .08);
  flex-shrink: 0;                             /* 고정된 크기를 유지하도록 설정 */
}

.container {
  flex-grow: 1;                   /* 나머지 공간을 차지하도록 설정 */
  background-color: rgba(255, 255, 255, 0.4);
  padding: 20px;                  /* 콘텐츠와 경계 사이에 여백 추가 */
  overflow-y: auto;               /* 콘텐츠가 넘칠 경우 스크롤바 추가 */
}

ul {
  list-style: none;
  padding-left: 30px;
  margin: 0;
}

/* 기본 스타일 */
.header_left .left_menu .menu_text {
  text-decoration: none;
  display: inline-block;
  font-size: 25px;
  line-height: 26px;
  font-weight: 400;
  letter-spacing: -1.18px;
  color: #303038;
  position: relative;         /* 밑줄을 위치시킬 공간을 마련 */
  transition: all 0.3s ease;  /* 부드러운 효과 추가 */

  padding-top: 12px;
  padding-bottom: 12px;
}

/* 활성화된 링크 스타일 */
.header_left .left_menu .menu_text.active-link {
  font-weight: 700;
  color: black;
  font-size: 26px;
}

/* 밑줄 스타일 */
.active-link::after {
  content: '';  /* 밑줄을 추가 */
  position: absolute;
  left: 0;
  bottom: -5px;  /* 메뉴 항목의 아래에 위치시킴 */
  width: 100%;
  height: 2px;  /* 밑줄 두께 */
  background-color: #1e1e23;  /* 밑줄 색상 */
  border-radius: 1px;  /* 밑줄 끝 모서리 둥글게 */
}

.left_item {
  padding: 10px;
}

@media (max-width: 1024px) {
  .wrap {
    flex-direction: column;     /* 모바일에서는 세로로 배치 */
  }

  .header {
    width: 100%;
    height: auto;               /* 모바일에서는 헤더 높이를 자동으로 맞춤 */
  }

  .container {
    width: 100%;                /* 모바일에서는 컨테이너가 화면 전체 너비 차지 */
    padding: 10px;
  }

}

</style>
