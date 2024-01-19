import React from 'react'

const UserDashBoard = () => {
    
  return (
    <div className='row p-5 mt-5'>
    <div className="card col-sm-5 p-0 mx-auto mb-5" >
    <h5 className="card-header bg-dark text-white">Exams</h5>
    <div className="card-body">
      <p style={{ "color": "#808B96" }}>*cick here to check the exams allocated for the student</p>
      <a href="/user-exam-dashboard" className="btn btn-primary mb-2" style={{"float":"right"}}>view</a>
    </div>
  </div>

  <div className="card col-sm-5 p-0 mx-auto mb-5">
    <h5 className="card-header bg-dark text-white ">Results</h5>
    <div className="card-body">
    <p style={{ "color": "#808B96" }}>*click here  to see your exam results</p>
      <a href="#" className="btn btn-primary position--0" style={{"float":"right"}}>view</a>
    </div>
  </div>
    </div>
  )
}

export default UserDashBoard;