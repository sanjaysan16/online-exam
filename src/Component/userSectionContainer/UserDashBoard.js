import React from 'react'

const UserDashBoard = () => {
    
  return (
    <div className='row p-5 mt-5 mx-0'>
    <div className="card col-sm-5 p-0 mx-auto mb-5" >
    <h5 className="card-header bg-dark text-white">Exams</h5>
    <div className="card-body">
      <p style={{ "color": "#808B96" }}>*To cick GoHere button you can see allotted exams fro you and you can take exam</p>
      <a href="/user-exam-dashboard" className="btn btn-primary mb-2" style={{"float":"right"}}>GoHere</a>
    </div>
  </div>

  <div className="card col-sm-5 p-0 mx-auto mb-5">
    <h5 className="card-header bg-dark text-white ">Results</h5>
    <div className="card-body">
    <p style={{ "color": "#808B96" }}>*To click GoHere  button you can see your exam results</p>
      <a href="#" className="btn btn-primary position--0" style={{"float":"right"}}>GoHere</a>
    </div>
  </div>
    </div>
  )
}

export default UserDashBoard;