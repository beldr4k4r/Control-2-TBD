<template>
  <div class="task-card" :class="{ 'is-completed': task.completed }">
    <div class="task-header">
      <h3>{{ task.title }}</h3>
      <span class="status-badge">{{ task.completed ? 'Completada' : 'Pendiente' }}</span>
    </div>

    <p class="description">{{ task.description }}</p>

    <div class="task-details">
      <p><strong>Vencimiento:</strong> {{ formatDate(task.dueDate) }}</p>
      <p><strong>Sector:</strong> {{ task.sector }}</p>
    </div>

    <div class="task-actions">
      <button v-if="!task.completed" @click="$emit('complete', task.id)" class="btn-complete">
        Completar
      </button>
      <button @click="$emit('edit', task.id)" class="btn-edit">Editar</button>
      <button @click="$emit('delete', task.id)" class="btn-delete">Eliminar</button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  task: {
    type: Object,
    required: true,
  },
})

defineEmits(['complete', 'edit', 'delete'])

const formatDate = (dateString) => {
  if (!dateString) return 'Sin fecha'
  const date = new Date(dateString)
  return date.toLocaleDateString('es-CL')
}
</script>

<style scoped>
.task-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  background-color: #fff;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.task-card.is-completed {
  border-left: 5px solid #4caf50;
  opacity: 0.8;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.status-badge {
  font-size: 0.8rem;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  background-color: #eee;
}

.is-completed .status-badge {
  background-color: #4caf50;
  color: white;
}

.description {
  color: #555;
  margin-bottom: 1rem;
}

.task-details p {
  margin: 0.2rem 0;
  font-size: 0.9rem;
}

.task-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

button {
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.btn-complete {
  background-color: #4caf50;
  color: white;
}
.btn-edit {
  background-color: #ffc107;
  color: black;
}
.btn-delete {
  background-color: #f44336;
  color: white;
}
</style>
