import React, { useState } from "react";
import { Link, Outlet } from 'react-router-dom';

import useStateRef from "react-usestateref";

function AddExam() {
  const [hasError, setHasError, refHasError] = useStateRef(false);



  function validation(key, value) {
    
    setHasError(false);
    var result = value;
    switch (key) {
      case "examName":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";

        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the examName";
          setHasError(true);
        }
        break;

     
      case "description":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the description";
          setHasError(true);
        }
        break;
      case "creationDate":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the creationdate";
          setHasError(true);
        }
        break;
      case "expirationDate":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the expirationdate";
          setHasError(true);
        }
        break;
      case "noOfQuestions":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the noofquestions";
          setHasError(true);
        }
        break;
      case "durationMinutes":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the durationminutes";
          setHasError(true);
        }
        break;
      case "passPercentage":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the passpercentage";
          setHasError(true);
        }
        break;
      case "questionsRandomized":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the questionsrandomized";
          setHasError(true);
        }
        break;
      case "answerMust":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the answermust";
          setHasError(true);
        }
        break;
      case "enableNegativeMarks":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the enablenegativemark";
          setHasError(true);
        }
        break;

      case "negativeMarkValues":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the negatiVemarkValue ";
          setHasError(true);
        }
        break;
    }
  }
// validation end


//onclick event......
  const examFormValidate = (e) => {
    e.preventDefault();

    console.log("validate event called");
    const data = new FormData(e.target);
    const dataObject = Object.fromEntries(data.entries());
    console.log(dataObject)
    Object.entries(dataObject).map(([key, value]) => {
       validation(key, value);
    });
    if (!refHasError.current) {
     

      fetch("https://localhost:8443/onlineexamapplication/control/addExam", {
        method: "POST",
        credentials: "include",
        body: JSON.stringify(dataObject),
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      }).then(response=>response.json())
      .then(value=>{console.log(value)});
    }

  };


  return (
    <>
      
      <div className="container-fluid pb-5 ">
        <div className="row">
          <div className="col-sm-7  offset-md-3 mt-5">
            <div className="card mt-5 card border-dark mb-3">
              <div className="card-header text-center">
                <h2>Add Exam</h2>
              </div>
              <div className="card-body">
                <form onSubmit={examFormValidate}>
                  <div className="form-group">
                    <label for="examname">Exam Name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="examName"
                      placeholder="Exam Name"
                      name="examName"
                    ></input>
                    <p id="examName_error" className="d-none text-danger"></p>
                  </div>

                  

                  <div className="row">
                    <div className="col-sm-6">
                      <div className="form-group">
                        <label for="description">Description</label>
                        <textarea
                          type="text"
                          className="form-control"
                          id="description"
                          placeholder="Write ur description"
                          name="description"
                        ></textarea>
                        <p
                          id="description_error"
                          className="d-none text-danger"
                        ></p>
                      </div>

                      <div className="form-group">
                        <div className="col-sm-6  ">
                          <label for="duration">Duration</label>
                          <input
                            type="text"
                            id="duration"
                            className="form-control"
                            placeholder="minutes"
                            name="durationMinutes"
                          ></input>
                          <p
                            id="durationMinutes_error"
                            className="d-none text-danger"
                          ></p>
                        </div>
                      </div>
                    </div>

                    <div className="col-sm-5 offset-md-1">
                      <div className="form-group">
                        <label for="creationdate">Creation Date</label>
                        <input
                          type="datetime-local"
                          className="form-control"
                          id="creationdate"
                          name="creationDate"
                        ></input>
                        <p
                          id="creationDate_error"
                          className="d-none text-danger"
                        ></p>
                      </div>

                      <div className="form-group">
                        <label for="expirationdate">Expiration Date</label>
                        <input
                          type="datetime-local"
                          className="form-control"
                          id="expirationdate"
                          name="expirationDate"
                        ></input>
                        <p
                          id="expirationDate_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col-sm-4 offset-md-0">
                      <div className="form-group">
                        <lable for="noof.qa">No of Question</lable>
                        <input
                          type="number"
                          className="form-control"
                          id="noof.qa"
                          name="noOfQuestions"
                        ></input>
                        <p
                          id="noOfQuestions_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>

                    <div className="col-sm-4 offset-md-3">
                      <label for="passpercentage">PassPercentage</label>
                      <input
                        type="number"
                        id="passpercentage"
                        className="form-control"
                        name="passPercentage"
                      ></input>
                      <p
                        id="passPercentage_error"
                        className="d-none text-danger"
                      ></p>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col-sm-6">
                      <div className="form-group">
                        <lable for="questionrandomized">
                          Question Ramdomized
                        </lable>
                        <input
                          type="text"
                          id="questionrandomized"
                          className="form-control"
                          placeholder="Question Randomized"
                          name="questionsRandomized"
                        ></input>
                        <p
                          id="questionsRandomized_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>

                    <div className="col-sm-5 offset-md-1">
                      <div className="form-group">
                        <lable for="answermust">Answer Must</lable>
                        <input
                          type="text"
                          id="answermust"
                          className="form-control"
                          placeholder="Answer Must"
                          name="answersMust"
                        ></input>
                        <p
                          id="answersMust_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col-sm-6 ">
                      <div className="form-group">
                        <label for="EnableNegativeMarks">
                          EnableNegativeMarks
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="EnableNegativeMarks"
                          name="enableNegativeMarks"
                        ></input>
                        <p
                          id="enableNegativeMarks_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>

                    <div className="col-sm-5 offset-md-1">
                      <div className="form-group">
                        <label for="nagativeMarksValues">
                          NegativeMarksValue
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="nagativemarksvalues"
                          name="negativeMarkValues"
                        ></input>
                        <p
                          id="negativeMarkValues_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                  <div className="text-center mt-2">
                    <button type='submit'className="btn btn-success">Submit</button>
                  <Link to='/add-topic'>  <button className="btn btn-primary offset-md-1">Add Topic</button></Link>
                  <Link to='#'><button className="btn btn-warning offset-md-1">View Exam</button></Link>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
  
    </>
  );
}

export default AddExam;
