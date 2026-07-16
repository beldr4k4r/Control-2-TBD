<template>
  <div class="task-list-container">
    <h2>Mis Tareas</h2>
    <div class="header-actions">
      <button @click="openNewTaskModal" class="btn-new-task">+ Nueva Tarea</button>
      <router-link to="/dashboard" class="btn-dashboard">Ver Estadísticas y Mapa</router-link>
      <button @click="logout" class="btn-logout">Cerrar Sesión</button>
    </div>

    <div v-if="urgentTasks.length > 0" class="notification-banner">
      <strong>¡Atención!</strong> Tienes {{ urgentTasks.length }} tarea(s) próxima(s) a vencer:
      <ul>
        <li v-for="task in urgentTasks" :key="'urgent-' + task.id">
          {{ task.title }} (Vence el {{ formatDate(task.dueDate) }})
        </li>
      </ul>
    </div>

    <TaskForm :isOpen="isModalOpen" :taskData="taskToEdit" :sectors="sectors" @close="closeModal" @save="saveTask" />

    <div class="filters">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Buscar por título o descripción..."
        class="search-input"
      />

      <select v-model="statusFilter" class="status-select">
        <option value="all">Todas</option>
        <option value="pending">Pendientes</option>
        <option value="completed">Completadas</option>
      </select>
    </div>

    <div class="task-grid" v-if="filteredTasks.length > 0">
      <TaskCard
        v-for="task in filteredTasks"
        :key="task.id"
        :task="task"
        @complete="markAsCompleted"
        @edit="editTask"
        @delete="deleteTask"
      />
    </div>

    <div v-else class="no-tasks">
      <p>No se encontraron tareas con esos filtros.</p>
    </div>
  </div>

</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import TaskCard from '../components/TaskC.vue'
import TaskForm from '../components/TaskF.vue'
import api from '../services/api.js'

const searchQuery = ref('')
const statusFilter = ref('all')

const tasks = ref([])
const sectors = ref([])
const isModalOpen = ref(false)
const taskToEdit = ref(null)

const logout = () => {
  localStorage.removeItem('jwt_token')
  window.location.href = '/login'
}

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
    console.error('Error al decodificar el token:', error)
    return null
  }
}

const fetchTasks = async () => {
  const userId = getUserIdFromToken()
  if (!userId) {
    alert('Sesión expirada o inválida. Por favor, inicia sesión nuevamente.')
    window.location.href = '/login'
    return
  }

  try {
    const params = {}

    if (statusFilter.value === 'completed') {
      params.completed = true
    } else if (statusFilter.value === 'pending') {
      params.completed = false
    }
    if (searchQuery.value && searchQuery.value.trim() !== '') {
      params.keyword = searchQuery.value.trim()
    }

    const response = await api.get('/tasks/filter', { params })

    if (Array.isArray(response.data)) {
      tasks.value = response.data.map(backendTask => {
        return {
          id: backendTask.id_task,
          title: backendTask.task_name,
          description: backendTask.description || backendTask.task_descrition || '',
          dueDate: backendTask.task_expired,
          completed: backendTask.complete_task,
          sector: backendTask.sector ? backendTask.sector.name : 'Sin sector asignado',
          rawSectorId: backendTask.sector ? backendTask.sector.id : null
        }
      })
    } else {
      tasks.value = []
    }
  } catch (error) {
    console.error('Error al cargar las tareas del backend:', error)
  }
}

const fetchSectors = async () => {
  try {
    const response = await api.get('/sectors')
    sectors.value = response.data
  } catch (error) {
    console.error('Error al cargar los sectores:', error)
  }
}

watch([searchQuery, statusFilter], () => {
  fetchTasks()
})

onMounted(() => {
  fetchTasks()
  fetchSectors()
})

const filteredTasks = computed(() => {
  return tasks.value
})

const urgentTasks = computed(() => {
  const today = new Date()
  const limitDate = new Date()
  limitDate.setDate(today.getDate() + 3)

  return tasks.value.filter((task) => {
    if (task.completed || !task.dueDate) return false
    const dueDate = new Date(task.dueDate)
    dueDate.setMinutes(dueDate.getMinutes() + dueDate.getTimezoneOffset())
    return dueDate <= limitDate
  })
})

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  date.setMinutes(date.getMinutes() + date.getTimezoneOffset())
  return date.toLocaleDateString('es-CL')
}

const markAsCompleted = async (taskId) => {
  try {
    await api.patch(`/tasks/${taskId}/complete`)
    await fetchTasks()
  } catch (error) {
    console.error("Error al completar la tarea:", error)
    alert('No se pudo marcar la tarea como completada.')
  }
}

const deleteTask = async (taskId) => {
  if (confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
    try {
      await api.delete(`/tasks/${taskId}`)
      await fetchTasks()
      alert('Tarea eliminada correctamente.')
    } catch (error) {
      console.error("Error al eliminar la tarea:", error)
      alert('Hubo un error al eliminar la tarea.')
    }
  }
}

const openNewTaskModal = () => {
  taskToEdit.value = null
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
}

const editTask = (taskId) => {
  const task = tasks.value.find((t) => t.id === taskId)
  if (task) {
    taskToEdit.value = { ...task }
    isModalOpen.value = true
  }
}

const saveTask = async (taskData) => {
  const userId = getUserIdFromToken()

  const payload = {
    task_name: taskData.title,
    task_descrition: taskData.description,
    task_expired: taskData.dueDate,
    complete_task: taskData.completed || false,
    user: { idAuth: userId },
    sector: { idSector: taskData.sectorId }
  }

  try {
    if (taskData.id) {
      await api.put(`/tasks/${taskData.id}`, payload)
      alert('Tarea actualizada exitosamente.')
    } else {
      await api.post('/tasks', payload)
      alert('Tarea creada exitosamente.')
    }
    await fetchTasks()
  } catch (error) {
    console.error("Error al guardar la tarea:", error)
    alert('Hubo un error al guardar los cambios.')
  }
}
</script>

<style scoped>
.task-list-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-input {
  flex: 1;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
}

.status-select {
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
  background-color: white;
}

.task-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.no-tasks {
  text-align: center;
  color: #666;
  margin-top: 3rem;
  font-size: 1.2rem;
}

.btn-new-task {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  margin-bottom: 1rem;
}

.btn-new-task:hover {
  background-color: #218838;
}

.header-actions {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.btn-dashboard {
  background-color: #17a2b8;
  color: white;
  text-decoration: none;
  padding: 0.6rem 1.2rem;
  border-radius: 4px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.btn-dashboard:hover {
  background-color: #138496;
}

.notification-banner {
  background-color: #fff3cd;
  color: #856404;
  border: 1px solid #ffeeba;
  padding: 1rem;
  border-radius: 6px;
  margin-bottom: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.notification-banner ul {
  margin: 0.5rem 0 0 0;
  padding-left: 1.5rem;
}

.notification-banner li {
  font-weight: bold;
}
.btn-logout {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.btn-logout:hover {
  background-color: #c82333;
}
</style>
