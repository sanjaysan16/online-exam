import React from 'react'

const AdminView = () => {
  return (
    <div className='p-5'>
    <div class="card" >
    <h5 class="card-header">Exams</h5>
    <div class="card-body">
      <h5 class="card-title">Create your Exams</h5>
      <p class="card-text"></p>
      <a href="/view-exam" class="btn btn-primary">Click to View Exams</a>
    </div>
  </div>

  <div class="card my-5">
    <h5 class="card-header">Student</h5>
    <div class="card-body">
      <h5 class="card-title">Create Students..</h5>
      <p class="card-text"></p>
      <a href="#" class="btn btn-primary">Click to View Students</a>
    </div>
  </div>
    </div>
  )
}

export default AdminView