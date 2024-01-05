import React from 'react'

const UserDashBoard = () => {
    
  return (
    <div className='row p-5 mt-5'>
    <div class="card col-sm-5 p-0 mx-auto mb-5" >
    <h5 class="card-header bg-dark text-white">Exams</h5>
    <div class="card-body">
      <h5 class="card-title">Here you can see and take exams</h5>
      <p class="card-text"></p>
      <a href="/view-exam" class="btn btn-primary ">Click to View Exams</a>
    </div>
  </div>

  <div class="card col-sm-5 p-0 mx-auto mb-5">
    <h5 class="card-header bg-dark text-white ">Student</h5>
    <div class="card-body">
      <h5 class="card-title">Create Students..</h5>
      <p class="card-text"></p>
      <a href="#" class="btn btn-primary">Click to View Students</a>
    </div>
  </div>
    </div>
  )
}

export default UserDashBoard;