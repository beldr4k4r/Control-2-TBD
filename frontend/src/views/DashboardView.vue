<template>
  <div class="dashboard-container">
    <router-link to="/tasks" class="btn-back">⬅ Volver a Mis Tareas</router-link>
    <h2>Dashboard de Estadísticas Espaciales</h2>
    <p class="subtitle">Resumen georreferenciado de operaciones</p>

    <div class="stats-grid">
      <div class="stat-card">
        <h3>Tareas por Sector</h3>
        <ul class="sector-list" v-if="stats.tasksBySector.length > 0">
          <li v-for="sector in stats.tasksBySector" :key="sector.name">
            <span>{{ sector.name }}</span>
            <strong>{{ sector.count }}</strong>
          </li>
        </ul>
        <p v-else class="note">No hay datos por sector.</p>
      </div>

      <div class="stat-card highlight-card">
        <h3>Tarea Pendiente Más Cercana</h3>
        <p class="task-name">{{ stats.nearestTask.title }}</p>
        <p class="note">Prioridad geográfica calculada por el sistema</p>
      </div>

      <div class="stat-card">
        <h3>Distancia Promedio</h3>
        <p class="big-number">{{ stats.averageDistance }}</p>
        <p class="note">Distancia relativa hacia tareas completadas</p>
      </div>

      <div class="stat-card warning-card">
        <h3>Sector Más Activo (5km)</h3>
        <p class="task-name">{{ stats.mostCongestedSector.name }}</p>
        <p class="note">
          Concentra <strong>{{ stats.mostCongestedSector.count }}</strong> tareas completadas cerca de ti
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api.js'

const stats = ref({
  tasksBySector: [],
  nearestTask: { title: 'Cargando...' },
  averageDistance: 0,
  mostCongestedSector: { name: 'Cargando...', count: 0 },
})

const getUserIdFromToken = () => {
  const token = localStorage.getItem('jwt_token')
  if (!token) return null
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload).id_auth
  } catch (error) {
    console.error('Error al decodificar token', error)
    return null
  }
}

const fetchDashboardStats = async () => {
  const userId = getUserIdFromToken()
  if (!userId) {
    alert('Sesión expirada')
    window.location.href = '/login'
    return
  }

  try {
    const sectorRes = await api.get(`/tasks/user/${userId}/by-sector`)
    stats.value.tasksBySector = sectorRes.data.map(item => ({
      name: item.sectorName,
      count: item.taskCount
    }))

    try {
      const nearestRes = await api.get(`/tasks/user/${userId}/nearest`)
      stats.value.nearestTask.title = nearestRes.data.task_name || 'Sin nombre'
    } catch (e) {
      stats.value.nearestTask.title = 'No hay tareas pendientes'
    }

    try {
      const avgRes = await api.get(`/tasks/user/${userId}/average-distance`)
      stats.value.averageDistance = parseFloat(avgRes.data).toFixed(2)
    } catch (e) {
      stats.value.averageDistance = '0.00'
    }

    try {
      const congestedRes = await api.get(`/tasks/user/${userId}/sector-most-completed?radiusKm=5`)
      stats.value.mostCongestedSector = {
        name: congestedRes.data.sectorName,
        count: congestedRes.data.taskCount
      }
    } catch (e) {
      stats.value.mostCongestedSector = { name: 'Sin datos en 5km', count: 0 }
    }

  } catch (error) {
    console.error('Error general al cargar el Dashboard:', error)
  }
}

onMounted(() => {
  fetchDashboardStats()
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
