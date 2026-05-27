<template>
  <div class="task-list-container">
    <h2>Mis Tareas</h2>

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

const markAsCompleted = (taskId) => {
  const task = tasks.value.find((t) => t.id === taskId)
  if (task) task.completed = true
}

const editTask = (taskId) => {
  alert(`Aquí abriremos el formulario para editar la tarea ${taskId}`)
}

const deleteTask = (taskId) => {
  if (confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
    tasks.value = tasks.value.filter((t) => t.id !== taskId)
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
</style>
