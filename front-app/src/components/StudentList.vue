<template>
  <div class='studentList'>
    <div>
      <el-button class="button"
                 @click="addNewStudent()">Add</el-button>
      <el-input placeholder="Input id for searching"
                v-model="searchedID"
                class="input_style">
        <el-button slot="append"
                   @click="searchByID()"
                   icon="el-icon-search"></el-button>
      </el-input>
    </div>
    <div align="center">
      <el-table class="table"
                :data="students">>
        <el-table-column prop="id"
                         label="ID"
                         width="100px">
        </el-table-column>
        <el-table-column prop="studentId"
                         label="Student ID"
                         width="200px">
        </el-table-column>
        <el-table-column prop="name"
                         label="Name"
                         width="200px">
        </el-table-column>
        <el-table-column prop="department"
                         label="Department"
                         width="150px">
        </el-table-column>
        <el-table-column prop="address"
                         label="Address"
                         width="400px">
        </el-table-column>
        <el-table-column prop="detail"
                         label="Detail">
          <template slot-scope="scope">
            <el-button type="info"
                       @click="checkDetail(scope.row.id)">Detail</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'student-table',
  data() {
    return {
      students: [],
      searchedID: ''
    }
  },

  mounted() {
    this.searchedID = ''
    this.searchByID()
  },

  methods: {
    async searchByID() {
      this.students = []
      if (this.searchedID === '') {
        // this.$axios.get('http://localhost:8083/api/students')
        //   .then(function (response) {
        //     for (var i = 0; i < response.data._embedded.students.total; i++) {
        //       this.students.push(response.data._embedded.students[i])
        //     }
        //     this.students = response.data._embedded.students
        //   }).catch(function (error) {
        //     console.log(error)
        //   })
        try {
          const response = await fetch('http://localhost:8083/api/students')
          const data = await response.json()
          this.students = data._embedded.students
        } catch (error) {
          console.log(error)
        }
      } else {
        // this.$axios({
        //   method: 'get',
        //   url: 'http://localhost:8083/api/students/' + this.searchedID,
        //   responseType: 'json'
        // })
        //   .then(function (response) {
        //   }).catch(function (error) {
        //     console.log(error)
        //   })
        try {
          const response = await fetch('http://localhost:8083/api/students/' + this.searchedID)
          const data = await response.json()
          this.students.push(data)
        } catch (error) {
          console.log(error)
        }
      }
    },

    addNewStudent() {
      this.$router.push('/studentAdd')
    },

    checkDetail(id) {
      this.$router.push({
        name: 'studentDetail',
        params: {
          id: id
        }
      }
      )
    }
  }
}

</script>

<style scope>
.studentList {
  left: 10%;
  right: 10%;
  bottom: 0;
  margin-top: 20px;
  overflow: hidden;
  background-color: white;
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

.button {
  margin-top: 20px;
  background-color: #009688;
  font-size: 20px;
  color: white;
  width: 120px;
  height: 40px;
  border: 0;
}

.table {
  margin-top: 10px;
  width: 80%;
}
</style>
