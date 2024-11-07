<template>
  <div class="outfit-recommendation">
    <h2>{{ title }}</h2>
    <div v-if="orderedCategories">
      <div v-for="category in orderedCategories" :key="category" class="category-section">
        <h3>{{ getCategoryName(category) }}</h3>
        <div class="outfit-items" v-if="outfits && outfits[category]">
          <button v-if="currentIndexes[category] > 0" @click="previousItems(category)" class="nav-button"> < </button>
          <div
              v-for="(item, index) in outfits[category].slice(currentIndexes[category], currentIndexes[category] + 3)"
              :key="item.outfitSeq"
              class="outfit-item"
              :class="{ highlighted: index === 0, selected: isSelected && isSelected(category, item.outfitSeq) }"
              @click="handleItemClick(category, item.outfitSeq)"
          >
            <img :src="getImageSrc(item.outfitSeq)" :alt="item.outfitName" class="outfit-image" />
            <p>{{ item.outfitName }}</p>
          </div>
          <button v-if="outfits[category].length > currentIndexes[category] + 3" @click="nextItems(category)" class="nav-button">></button>
        </div>
      </div>
    </div>
    <button v-if="showSaveButton" @click="emitSaveSelection" class="save-button">선택 완료</button>
  </div>
</template>

<script>
export default {
  name: 'OutfitRecommendationList',
  props: {
    title: String,
    outfits: Object,
    isSelected: Function,
    getImageSrc: Function,
    showSaveButton: Boolean
  },
  data() {
    return {
      currentIndexes: {
        TOP: 0,
        BOTTOM: 0,
        OUTERWEAR: 0,
        SHOES: 0,
        ACCESSORY: 0
      },
      orderedCategories: ["TOP", "BOTTOM", "OUTERWEAR", "SHOES", "ACCESSORY"],
    };
  },
  methods: {
    getCategoryName(category) {
      const categoryMap = {
        TOP: "상의",
        BOTTOM: "하의",
        OUTERWEAR: "아우터",
        SHOES: "신발",
        ACCESSORY: "악세사리"
      };
      return categoryMap[category] || category;
    },
    nextItems(category) {
      const maxIndex = this.outfits[category]?.length || 0;
      if (this.currentIndexes[category] + 3 < maxIndex) {
        this.currentIndexes[category] += 3;
      }
    },
    previousItems(category) {
      if (this.currentIndexes[category] - 3 >= 0) {
        this.currentIndexes[category] -= 3;
      }
    },
    handleItemClick(category, outfitSeq) {
      this.$emit('itemClicked', category, outfitSeq);
    },
    emitSaveSelection() {
      this.$emit('saveSelection');
    }
  }
};
</script>

<style scoped>
.outfit-recommendation {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  max-width: 800px;
  margin: auto;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.category-section {
  margin-bottom: 20px;
  width: 100%;
}

.outfit-items {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.outfit-item {
  text-align: center;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  cursor: pointer;
}

.outfit-item.highlighted {
  border: 2px solid #007bff;
  background-color: #e0f7ff;
}

.outfit-item.selected {
  background-color: #007bff;
  color: white;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.4);
  transform: scale(1.05);
}

.outfit-image {
  width: 80px;
  height: 80px;
  margin-bottom: 8px;
}

.outfit-item p {
  font-size: 1rem;
  margin: 0;
  color: #333;
}

.nav-button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-button:hover {
  background-color: #0056b3;
}

.save-button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.save-button:hover {
  background-color: #0056b3;
}
</style>
