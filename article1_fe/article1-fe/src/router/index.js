import mainRouter from "@/router/mainRouter.js";
import boardRouter from "@/router/boardRouter.js";
import userRouter from "@/router/userRouter.js"
import mypageRouter from "@/router/mypageRouter.js"
import MapWithDateView from "@/views/outfit/setting/MapWithDateView.vue";
import SelectSituation from "@/views/outfit/setting/SituationView.vue";
import {createRouter, createWebHistory} from "vue-router";
import OutfitRecommendationResult from "@/views/outfit/recommend/OutfitRecommendationResult.vue";
import GuestOutfitRecommendation from "@/components/outfit/recommend/GuestOutfitRecommendation.vue";
import UserOutfitRecommendation from "@/components/outfit/recommend/UserOutfitRecommendation.vue";

const routes = [
    {
        path: "/map",
        name: "MapWithDateView",
        component: MapWithDateView,
    },
    {
        path: "/map/select-situation",
        name: "SelectSituation",
        component: SelectSituation,
    },
    {
        path: "/guest/outfit/recommendation",
        name: "GuestOutfitRecommendation",
        component: GuestOutfitRecommendation,
    },
    {
        path: "/user/outfit/recommendations", // 회원용 복장 추천 경로 추가
        name: "UserOutfitRecommendation",
        component: UserOutfitRecommendation,
    },

    // 메인 라우터
    ...mainRouter,
    // 게시판 라우터
    ...boardRouter,
    // 회원 라우터
    ...userRouter,
    // 마이페이지 라우터
    ...mypageRouter
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    // 라우팅 시 화면 최 상단으로 이동됨.
    scrollBehavior(to, from, savedPosition) {
        // savedPosition이 있는 경우(예: 뒤로 가기), 해당 위치로 이동
        if (savedPosition) {
            return savedPosition;
        } else {
            // 새로운 페이지 이동 시 맨 위로 스크롤
            return {top: 0};
        }
    },
});

export default router;