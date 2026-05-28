<template>
  <div class="dashboard-container">
    <router-link to="/tasks" class="btn-back">⬅ Volver a Mis Tareas</router-link>
    <h2>Dashboard de Estadísticas Espaciales</h2>
    <p class="subtitle">Resumen georreferenciado de operaciones</p>

    <div class="stats-grid">
      <div class="stat-card">
        <h3>Tareas por Sector</h3>
        <ul class="sector-list">
          <li v-for="sector in stats.tasksBySector" :key="sector.name">
            <span>{{ sector.name }}</span>
            <strong>{{ sector.count }}</strong>
          </li>
        </ul>
      </div>

      <div class="stat-card highlight-card">
        <h3>Tarea Más Cercana</h3>
        <p class="task-name">{{ stats.nearestTask.title }}</p>
        <p class="distance">
          A <strong>{{ stats.nearestTask.distance }} km</strong> de tu ubicación
        </p>
      </div>

      <div class="stat-card">
        <h3>Tarea Más Lejana</h3>
        <p class="task-name">{{ stats.furthestTask.title }}</p>
        <p class="distance">
          A <strong>{{ stats.furthestTask.distance }} km</strong> de distancia
        </p>
      </div>

      <div class="stat-card">
        <h3>Distancia Promedio</h3>
        <p class="big-number">{{ stats.averageDistance }} <span class="unit">km</span></p>
        <p class="note">Promedio hacia tareas pendientes</p>
      </div>

      <div class="stat-card">
        <h3>En tu Radar (Radio 5km)</h3>
        <p class="big-number">{{ stats.tasksInRadius }}</p>
        <p class="note">Tareas operativas en tu zona</p>
      </div>

      <div class="stat-card warning-card">
        <h3>Sector Crítico</h3>
        <p class="task-name">{{ stats.mostCongestedSector.name }}</p>
        <p class="note">
          Concentra el <strong>{{ stats.mostCongestedSector.percentage }}%</strong> de los
          pendientes
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const stats = ref({
  tasksBySector: [
    { name: 'Calles', count: 12 },
    { name: 'Construcción', count: 5 },
    { name: 'Reparación de Semáforos', count: 8 },
    { name: 'Parques y Jardines', count: 3 },
  ],
  nearestTask: {
    title: 'Reparar semáforo Alameda',
    distance: 1.2,
  },
  furthestTask: {
    title: 'Bacheo en periferia',
    distance: 14.5,
  },
  averageDistance: 5.8,
  tasksInRadius: 7,
  mostCongestedSector: {
    name: 'Calles',
    percentage: 42,
  },
})
</script>

<style scoped>
.dashboard-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

h2 {
  color: #333;
  margin-bottom: 0.2rem;
}

.subtitle {
  color: #666;
  margin-bottom: 2rem;
  font-size: 1.1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.stat-card {
  background: white;
  border-radius: 10px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #eaeaea;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.card-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

h3 {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 1rem;
  width: 100%;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
}

.sector-list {
  list-style: none;
  padding: 0;
  width: 100%;
}

.sector-list li {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px dashed #eee;
}

.sector-list li:last-child {
  border-bottom: none;
}

.task-name {
  font-weight: bold;
  font-size: 1.2rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.distance,
.note {
  color: #777;
  font-size: 0.95rem;
}

.big-number {
  font-size: 3rem;
  font-weight: bold;
  color: #007bff;
  margin: 0.5rem 0;
}

.unit {
  font-size: 1.2rem;
  color: #666;
}

.highlight-card {
  border-top: 4px solid #28a745;
}

.warning-card {
  border-top: 4px solid #dc3545;
}

.btn-back {
  display: inline-block;
  margin-bottom: 1.5rem;
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
  font-size: 1.1rem;
}

.btn-back:hover {
  text-decoration: underline;
}
</style>
