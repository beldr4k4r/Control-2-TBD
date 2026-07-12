<template>
  <div class="task-list-container">
    <h2>Mis Tareas</h2>
    <div class="header-actions">
      <button @click="openNewTaskModal" class="btn-new-task">+ Nueva Tarea</button>
      <router-link to="/dashboard" class="btn-dashboard">Ver Estadísticas y Mapa</router-link>
    </div>

    <div v-if="urgentTasks.length > 0" class="notification-banner">
      <strong>¡Atención!</strong> Tienes {{ urgentTasks.length }} tarea(s) próxima(s) a vencer:
      <ul>
        <li v-for="task in urgentTasks" :key="'urgent-' + task.id">
          {{ task.title }} (Vence el {{ formatDate(task.dueDate) }})
        </li>
      </ul>
    </div>

    <TaskForm :isOpen="isModalOpen" :taskData="taskToEdit" @close="closeModal" @save="saveTask" />

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
import { ref, computed } from 'vue'
import TaskCard from '../components/TaskC.vue'
import TaskForm from '../components/TaskF.vue'

const searchQuery = ref('')
const statusFilter = ref('all')

const tasks = ref([
  {
    id: 1,
    title: 'Reparar semáforo',
    description: 'El semáforo de Alameda con Matucana no tiene luz roja.',
    dueDate: '2026-05-30',
    sector: 'reparación de semáforos',
    completed: false,
  },
  {
    id: 2,
    title: 'Bacheo de calle',
    description: 'Tapar hoyo peligroso en pista derecha.',
    dueDate: '2026-05-28',
    sector: 'calles',
    completed: true,
  },
  {
    id: 3,
    title: 'Inspección de obra',
    description: 'Revisar avances de la nueva construcción.',
    dueDate: '2026-06-05',
    sector: 'construcción',
    completed: false,
  },
])

const filteredTasks = computed(() => {
  return tasks.value.filter((task) => {
    const matchesStatus =
      statusFilter.value === 'all' ||
      (statusFilter.value === 'completed' && task.completed) ||
      (statusFilter.value === 'pending' && !task.completed)

    const lowerCaseQuery = searchQuery.value.toLowerCase()
    const matchesSearch =
      task.title.toLowerCase().includes(lowerCaseQuery) ||
      task.description.toLowerCase().includes(lowerCaseQuery)

    return matchesStatus && matchesSearch
  })
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

const markAsCompleted = (taskId) => {
  const task = tasks.value.find((t) => t.id === taskId)
  if (task) task.completed = true
}

const deleteTask = (taskId) => {
  if (confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
    tasks.value = tasks.value.filter((t) => t.id !== taskId)
  }
}

const isModalOpen = ref(false)
const taskToEdit = ref(null)

const openNewTaskModal = () => {
  taskToEdit.value = null
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
}

const saveTask = (taskData) => {
  if (taskData.id) {
    const index = tasks.value.findIndex((t) => t.id === taskData.id)
    if (index !== -1) {
      tasks.value[index] = taskData
    }
  } else {
    taskData.id = Date.now()
    taskData.completed = false
    tasks.value.push(taskData)
  }
}

const editTask = (taskId) => {
  const task = tasks.value.find((t) => t.id === taskId)
  if (task) {
    taskToEdit.value = { ...task }
    isModalOpen.value = true
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
</style>
