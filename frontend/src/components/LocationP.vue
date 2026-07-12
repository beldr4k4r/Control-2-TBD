<template>
  <div class="map-container">
    <p class="map-instruction">Haz clic en el mapa para fijar tu ubicación:</p>
    <div
      style="
        height: 300px;
        width: 100%;
        border-radius: 8px;
        overflow: hidden;
        border: 1px solid #ccc;
      "
    >
      <l-map ref="map" v-model:zoom="zoom" :center="center" @click="addMarker">
        <l-tile-layer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          layer-type="base"
          name="OpenStreetMap"
        ></l-tile-layer>
        <l-marker v-if="marker" :lat-lng="marker"></l-marker>
      </l-map>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import 'leaflet/dist/leaflet.css'
import { LMap, LTileLayer, LMarker } from '@vue-leaflet/vue-leaflet'

const emit = defineEmits(['update:location'])

const zoom = ref(13)
// Coordenadas centrales por defecto (Santiago)
const center = ref([-33.4489, -70.6693])
const marker = ref(null)

const addMarker = (event) => {
  const { lat, lng } = event.latlng
  marker.value = [lat, lng]

  emit('update:location', { lat, lng })
}
</script>

<style scoped>
.map-container {
  margin-bottom: 1rem;
}
.map-instruction {
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 0.5rem;
  font-style: italic;
}
</style>
