<template>
  <div class='studentDetail'>
    <h3 class='page_title'>Detail</h3>

    <div v-if="editing === this.id">
      <el-form :model="studentForm"
               :rules="studentRule"
               ref="studentForm">
        <table class="info_table">
          <tr>
            <td align="right">Student ID：</td>
            <td align="left">
              <el-form-item prop="studentId">
                <el-input v-model="studentForm.studentId"
                          clearable
                          class="input_style"></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">FirstName：</td>
            <td align="left">
              <el-form-item prop="firstName">
                <el-input v-model="studentForm.firstName"
                          clearable
                          class="input_style"></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">LastName: </td>
            <td align="left">
              <el-form-item prop="lastName">
                <el-input v-model="studentForm.lastName"
                          clearable
                          class="input_style"></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">Sex：</td>
            <td align="left">
              <el-form-item prop="sex">
                <el-select v-model="studentForm.sex"
                           placeholder="select sex"
                           class="input_style">
                  <el-option label="male"
                             value="male"></el-option>
                  <el-option label="female"
                             value="female"></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">BirthDate：</td>
            <td align="left">
              <el-form-item prop="birthDate">
                <el-date-picker type="date"
                                value-format="yyyy-MM-dd"
                                v-model="studentForm.birthDate"
                                placeholder="select date"
                                class="input_style"></el-date-picker>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">Address：</td>
            <td align="left">
              <el-form-item prop="address">
                <el-input v-model="studentForm.address"
                          clearable
                          class="input_style"></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td align="right">Department：</td>
            <td align="left">
              <el-form-item prop="department">
                <el-input v-model="studentForm.department"
                          clearable
                          class="input_style"></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
        <el-button class="edit_button"
                   type="primary"
                   @click="editStudent('studentForm')">Save</el-button>
        <el-button class="edit_button"
                   type="info"
                   @click="cancelEdit()">Cancel</el-button>
      </el-form>
    </div>

    <div v-else>
      <table class="info_table">
        <tr>
          <td align="right">Student ID：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.studentId}}</td>
        </tr>
        <tr>
          <td align="right">FirstName：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.firstName}}</td>
        </tr>
        <tr>
          <td align="right">LastName: </td>
          <td align="left"
              v-on="studentForm">{{studentForm.lastName}}</td>
        </tr>
        <tr>
          <td align="right">Sex：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.sex}}</td>
        </tr>
        <tr>
          <td align="right">BirthDate：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.birthDate}}</td>
        </tr>
        <tr>
          <td align="right">Address：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.address}}</td>
        </tr>
        <tr>
          <td align="right">Department：</td>
          <td align="left"
              v-on="studentForm">{{studentForm.department}}</td>
        </tr>
      </table>
    </div>

    <div v-if="editing === null">
      <el-button class="edit_button"
                 v-on:click="editMode()">Edit</el-button>
      <el-button class="edit_button"
                 v-on:click="deleteStudent()">Delete</el-button>
      <el-button class="edit_button"
                 v-on:click="back()">Back</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'student-detail',
  data() {
    return {
      editing: null,
      id: this.$route.params.id,
      studentForm: {
        studentId: '',
        firstName: '',
        lastName: '',
        sex: '',
        birthDate: '',
        address: '',
        department: ''
      },
      studentRule: {
        studentId: [
          { required: true, message: 'please input student ID', trigger: 'blur' },
          { pattern: /^\d{9}$/, message: 'expect 9 numbers', trigger: 'blur' }
        ],
        firstName: [
          { required: true, message: 'please input firstname', trigger: 'blur' }
        ],
        lastName: [
          { required: true, message: 'please input lastname', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: 'please select sex', trigger: 'blur' }
        ],
        birthDate: [
          { pattern: /^\d{4}-\d{2}-\d{2}$/, required: true, message: 'please select date', trigger: 'blur' }
        ],
        address: [
          { required: true, message: 'please input address', trigger: 'blur' }
        ],
        department: [
          { required: true, message: 'please input department', trigger: 'blur' }
        ]
      }
    }
  },

  mounted() {
    this.getDetail()
  },

  methods: {

    async getDetail() {
      try {
        const response = await fetch('http://localhost:8083/api/students/' + this.id)
        const data = await response.json()
        this.studentForm = data
      } catch (error
      ) {
        console.log(error)
      }
    },

    editMode() {
      this.cachedStudent = Object.assign({}, this.studentForm)
      this.editing = this.id
    },
    cancelEdit() {
      Object.assign(this.studentForm, this.cachedStudent)
      this.editing = null
    },

    editStudent(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            const response = await fetch('http://localhost:8083/api/students/' + this.id, {
              method: 'PUT',
              headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(this.studentForm)
            })
            if (response.ok) {
              alert('edit success!')
              this.editing = null
            } else {
              console.log(response.status)
            }
          } catch (error) {
            console.log(error)
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },

    async deleteStudent() {
      try {
        const response = await fetch('http://localhost:8083/api/students/' + this.id, {
          method: 'DELETE',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.studentForm)
        })
        if (response.ok) {
          alert('delete success!')
          this.$router.push('/')
        } else {
          console.log(response.status)
        }
      } catch (error) {
        console.log(error)
      }
    },

    back() {
      this.$router.push('/')
    }
  }
}
</script>

<style scope>
.studentDetail {
  left: 10%;
  right: 10%;
  bottom: 0;
  margin-top: 20px;
  overflow: hidden;
  background-color: white;
}

.page_title {
  margin-top: 10px;
  margin-left: 20px;
  font-size: 20px;
  text-align: left;
}

.info_table {
  margin-top: 20px;
  margin-left: 40px;
}

.input_style {
  margin-top: 20px;
  margin-left: 5%;
  background-color: white;
  width: 600px;
  margin-bottom: 10px;
  border-radius: 6px;
  border: 1px solid #888888;
}

.edit_button {
  margin-top: 20px;
  margin-bottom: 20px;
  align-content: left;
  background-color: #009688;
  font-size: 20px;
  color: white;
  width: 120px;
  height: 40px;
  border: 0;
}
</style>
