<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>{{ isEditing ? 'Editar Tarea' : 'Nueva Tarea' }}</h2>

      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Título:</label>
          <input type="text" v-model="form.title" required placeholder="Ej: Tapar bache" />
        </div>

        <div class="form-group">
          <label>Descripción:</label>
          <textarea
            v-model="form.description"
            required
            placeholder="Detalles de la tarea..."
          ></textarea>
        </div>

        <div class="form-group">
          <label>Fecha de Vencimiento:</label>
          <input type="date" v-model="form.dueDate" required />
        </div>

        <div class="form-group">
          <label>Sector Asociado:</label>
          <select v-model="form.sectorId" required>
            <option value="" disabled>Selecciona un sector...</option>
            <option v-for="sector in sectors" :key="sector.idSector" :value="sector.idSector">
              {{ sector.sectorName }}
            </option>
          </select>
        </div>

        <div class="form-actions">
          <button type="button" class="btn-cancel" @click="$emit('close')">Cancelar</button>
          <button type="submit" class="btn-save">Guardar Tarea</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  isOpen: Boolean,
  taskData: {
    type: Object,
    default: null,
  },
  sectors: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  title: '',
  description: '',
  dueDate: '',
  sectorId: '',
})

const isEditing = computed(() => !!props.taskData)

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal) {
      if (props.taskData) {
        form.value = { 
          id: props.taskData.id,
          title: props.taskData.title,
          description: props.taskData.description,
          dueDate: props.taskData.dueDate,
          sectorId: props.taskData.rawSectorId || ''
        }
      } else {
        form.value = { title: '', description: '', dueDate: '', sectorId: '' }
      }
    }
  },
)

const handleSubmit = () => {
  emit('save', { ...form.value })
  emit('close')
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input,
textarea,
select {
  width: 100%;
  padding: 0.6rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.btn-cancel {
  background-color: #ccc;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save:hover {
  background-color: #0056b3;
}
</style>
