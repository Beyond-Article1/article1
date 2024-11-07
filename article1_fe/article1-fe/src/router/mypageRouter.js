import UserDetail from "@/components/user/UserDetail.vue";
import MyPageLayout from "@/views/user/MyPageLayout.vue";
import UserPreference from "@/components/user/UserPreference.vue";
import UserOutfitList from "@/components/user/UserOutfitList.vue";
import UserOutfitStats from "@/components/user/UserOutfitStats.vue";
import UserReview from "@/components/user/UserReview.vue";
import UserPost from "@/components/user/UserPost.vue";

export default [
    {
        path: '/mypage',
        component: MyPageLayout, // MyPageLayout 컴포넌트로 감싸서 모든 콘텐츠 처리
        children: [
            {
                // 회원 정보
                path: 'detail',
                component: UserDetail
            },
            {
                // 회원 선호도
                path: 'preference',
                component: UserPreference
            },
            {
                // 회원 아웃핏 이력 조회
                path: 'outfit/list',
                component: UserOutfitList
            },
            {
                // 회원 아웃핏 통계
                path: 'outfit/stats',
                component: UserOutfitStats
            },
            {
                // 회원 리뷰
                path: 'review',
                component: UserReview
            },
            {
                // 회원 게시글
                path: 'post',
                component: UserPost
            }
        ]
    }
];