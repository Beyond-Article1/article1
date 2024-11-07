import MainView from "@/views/main/MainView.vue";
import WeatherTab from "@/components/WeatherTab.vue";

export default [
    {
        path: '/',
        component: MainView     // 메인화면 페이지 
    },
    {
        path: '/weather-component',
        component: WeatherTab
    }
];