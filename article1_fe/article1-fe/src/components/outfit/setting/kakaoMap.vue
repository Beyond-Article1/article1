<template>
  <div class="map-container square-component">
    <div class="search-overlay">
      <div class="search-bar">
        <input type="text" v-model="searchKeyword" placeholder="장소를 검색하세요" />
        <button @click="searchPlaces">검색</button>
      </div>
      <div v-if="places.length && showResults" class="search-results">
        <button class="toggle-button" @click="toggleResults">접기</button>
        <ul>
          <li v-for="(place, index) in places" :key="index" @click="moveToPlace(place)">
            <div class="place-info">
              <strong>{{ place.place_name }}</strong>
              <p>{{ place.address_name }}</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div id="map" ref="map" class="map"></div>
  </div>
</template>

<script>
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';

export default {
  name: "KakaoMap",
  data() {
    return {
      map: null,
      searchKeyword: "",
      places: [],
      currentMarker: null,
      locationSelected: false,
      infoWindow: null,
      showResults: true, // 검색 결과 표시 상태
    };
  },
  mounted() {
    this.loadKakaoMapScript()
        .then(() => {
          this.initMapWithAccurateLocation();
        })
        .catch((error) => {
          console.error("Kakao Map 스크립트 로드에 실패했습니다:", error);
        });
  },
  methods: {
    loadKakaoMapScript() {
      return new Promise((resolve, reject) => {
        if (window.kakao && window.kakao.maps) {
          resolve();
          return;
        }

        const kakaoApiKey = import.meta.env.VITE_KAKAO_API_KEY;
        if (!kakaoApiKey) {
          reject("Kakao API Key가 설정되지 않았습니다.");
          return;
        }

        const script = document.createElement("script");
        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoApiKey}&libraries=services&autoload=false`;
        script.onload = () => {
          kakao.maps.load(resolve);
        };
        script.onerror = () => {
          reject(new Error("Kakao Map 스크립트 로드에 실패했습니다."));
        };
        document.head.appendChild(script);
      });
    },
    initMapWithAccurateLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
              const latitude = position.coords.latitude;
              const longitude = position.coords.longitude;
              this.initMap(latitude, longitude);
              this.addCurrentLocationMarker(latitude, longitude);
            },
            () => {
              this.initMap(37.5665, 126.978);
            },
            { enableHighAccuracy: true }
        );
      } else {
        this.initMap(37.5665, 126.978);
      }
    },
    initMap(latitude, longitude) {
      const container = this.$refs.map;
      const options = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3,
      };
      this.map = new kakao.maps.Map(container, options);

      kakao.maps.event.addListener(this.map, "click", (mouseEvent) => {
        const latlng = mouseEvent.latLng;
        this.locationSelected = true;
        this.addMarker(latlng);

        const store = useSelectedInfoStore();
        store.setLocation(latlng.getLat(), latlng.getLng());
      });
    },
    addCurrentLocationMarker(latitude, longitude) {
      this.addMarker(new kakao.maps.LatLng(latitude, longitude));
    },
    addMarker(position) {
      if (this.currentMarker) {
        this.currentMarker.setMap(null);
      }
      if (this.infoWindow) {
        this.infoWindow.close();
      }

      this.currentMarker = new kakao.maps.Marker({
        position: position,
      });
      this.currentMarker.setMap(this.map);

      this.infoWindow = new kakao.maps.InfoWindow({ zIndex: 1 });
      kakao.maps.event.addListener(this.currentMarker, "click", () => {
        this.displayLocationInfo(position);
      });
    },
    displayLocationInfo(position) {
      const geocoder = new kakao.maps.services.Geocoder();

      geocoder.coord2Address(position.getLng(), position.getLat(), (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          const detailAddr = !!result[0].road_address
              ? result[0].road_address.address_name
              : result[0].address.address_name;

          const content = `<div style="padding:5px;font-size:0.9rem;">${detailAddr}</div>`;
          this.infoWindow.setContent(content);
          this.infoWindow.open(this.map, this.currentMarker);
        } else {
          console.error("주소를 가져오는 데 실패했습니다.");
        }
      });
    },
    searchPlaces() {
      if (!this.searchKeyword.trim()) {
        alert("검색어를 입력하세요.");
        return;
      }

      const places = new kakao.maps.services.Places();
      places.keywordSearch(this.searchKeyword, (data, status) => {
        if (status === kakao.maps.services.Status.OK) {
          this.places = data;
          this.showResults = true;

          const bounds = new kakao.maps.LatLngBounds();
          data.forEach((place) => {
            bounds.extend(new kakao.maps.LatLng(place.y, place.x));
          });
          this.map.setBounds(bounds);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
          alert("검색 결과가 없습니다.");
        } else {
          alert("검색 중 오류가 발생했습니다.");
        }
      });
    },
    toggleResults() {
      this.showResults = !this.showResults;
    },
    moveToPlace(place) {
      const position = new kakao.maps.LatLng(place.y, place.x);
      this.map.setCenter(position);
      this.addMarker(position);

      this.locationSelected = true;
      const store = useSelectedInfoStore();
      store.setLocation(place.y, place.x);
    },
  },
};
</script>

<style scoped>
.map-container {
  position: relative;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.square-component {
  width: 470px;
  height: 470px;
}

.search-overlay {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 90%;
  max-width: 250px;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 10px;
  border-radius: 8px;
  z-index: 2;
  display: flex;
  flex-direction: column;
}

.search-bar {
  display: flex;
  width: 100%;
}

.search-bar input[type="text"] {
  flex: 1;
  padding: 8px;
  font-size: 0.9rem;
  border-radius: 4px 0 0 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

.search-bar button {
  padding: 8px 12px;
  font-size: 0.9rem;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  height: auto;
  box-sizing: border-box;
}

.search-results {
  max-height: 200px;
  overflow-y: auto;
  background-color: rgba(255, 255, 255, 0.8); /* 반투명 배경색 */
  border-radius: 0 0 8px 8px;
  border-top: 1px solid #ddd;
  margin-top: 5px;
  padding: 5px;
  position: relative;
}

.search-results ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.search-results li {
  display: flex;
  padding: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.search-results li:hover {
  background-color: #f0f0f0;
}

.toggle-button {
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  padding: 5px 10px;
  margin-bottom: 5px;
  width: 100%;
  font-size: 0.9rem;
}

.place-info strong {
  font-weight: bold;
  font-size: 1rem;
}

.map {
  width: 100%;
  height: 100%;
}
</style>
