import React, { useEffect, useState } from "react";
import Swal from "sweetalert2";

import useStateRef from "react-usestateref";
import { useNavigate } from 'react-router-dom';
import { port, protocol } from "../fetchConst";


const AddExam = () => {

  const [hasError, setHasError, refHasError] = useStateRef(false);

  const uri = `${protocol}://${window.location.hostname}:${port}`;


  const [examName, setExamName] = useState('');
  const [description, setDescription] = useState('');
  const [creationdate, setCreationDate] = useState('');
  const [expirationdate, setExpirationDate] = useState('');
  const [noOfQuestions, setNoOfquestions] = useState('');
  const [durationMinutes, setDurationMinutes] = useState('');
  const [passPercentage, setPassPercentage] = useState('');
  const [questionsRandomized, setQuestionsRandomized] = useState('');
  const [answerMust, setAnswerMust] = useState('')
  const [enableNegativeMark, setEnableNegativeMark] = useState('');
  const [negativeMarkValue, setNegativeMarkValue] = useState('');

  var url = window.location.href.includes('?');
  var queryParam = window.location.search;

  const queryParamKeypairs = new URLSearchParams(queryParam);

  var examId = queryParamKeypairs.get('examId');

  useEffect(() => {
    console.log(url);
    if (url) {

      console.log(examId);

      examFetch();

    }

  }, []);


  const examFetch = async () => {
    const examFetchFunc = await fetch(`${uri}/onlineexamapplication/control/get-exam-or-exam-list`, {
      method: 'POST',
      credentials: 'include',
      body: JSON.stringify({ examId: examId }),
      headers: {
        'Content-Type': "application/json",
        'Accept': "application/json"
      }
    })
    const examFetchResultJson = await examFetchFunc.json();
    const examFetchResult = examFetchResultJson.getExam;
    console.log(examFetchResult);
    setExamName(examFetchResult.examName);
    setDescription(examFetchResult.description)
    setCreationDate(examFetchResult.creationDate)
    setExpirationDate(examFetchResult.expirationDate)
    setNoOfquestions(examFetchResult.noOfQuestions)
    setDurationMinutes(examFetchResult.durationMinutes)
    setEnableNegativeMark(examFetchResult.enableNegativeMark)
    setPassPercentage(examFetchResult.passPercentage)
    setNegativeMarkValue(examFetchResult.negativeMarkValue)
    setQuestionsRandomized(examFetchResult.questionsRandomized)
    setAnswerMust(examFetchResult.answersMust)


  }



  function validation(key, value) {
    var result = value;

    switch (key) {

      case "examName":
        console.log(result);
        if (!result) {
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the examName";
          setHasError(true);
        } else {
          if (result.match(/^[a-z\s*A-z\s*0-9|s*]+$/g)) {
            document.getElementById(key + "_error").classList.remove("d-block");
            document.getElementById(key + "_error").classList.add("d-none");
            document.getElementById(key + "_error").innerHTML = "";

          } else {
            document.getElementById(key + "_error").classList.remove("d-none");
            document.getElementById(key + "_error").classList.add("d-block");
            document.getElementById(key + "_error").innerHTML =
              "*special characters not alloewd";
            setHasError(true);
          }
        }
        break;
      case "description":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          setHasError(true);
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the description";

        }
        break;
      case "creationDate":
        document.getElementById(key + "_error").classList.remove("d-block");
        document.getElementById(key + "_error").classList.add("d-none");
        document.getElementById(key + "_error").innerHTML = "";
        if (!result) {
          setHasError(true);
          document.getElementById(key + "_error").classList.remove("d-none");
          document.getElementById(key + "_error").classList.add("d-block");
          document.getElementById(key + "_error").innerHTML =
            "*please enter the creationdate";

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
      case "enableNegativeMark":
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

      case "negativeMarkValue":
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

  const navigate = useNavigate();
  //onclick event......
  const examFormValidate = (e) => {
    e.preventDefault();
    setHasError(false)

    console.log("validate event called");
    const data = new FormData(e.target);
    const dataObject = Object.fromEntries(data.entries());
    console.log(dataObject)
    console.log(creationdate)
    console.log(expirationdate)
    Object.entries(dataObject).map(([key, value]) => {
      validation(key, value);

    });

    console.log(hasError + "...haserror")
    console.log(refHasError + "..ref")
    console.log(refHasError.current + "..........current")

    if (!refHasError.current) {
      if (url) {
        console.log("thi is...............................")
        dataObject.examId = examId;
        console.log("dataObject---dataObject", dataObject)
        fetch(`${uri}/onlineexamapplication/control/update-exam`, {
          method: "POST",
          credentials: "include",
          body: JSON.stringify(dataObject),
          headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
        }).then(response => response.json())
          .then(value => {
            if (value.result == "success") {
              Swal.fire({
                position: "top-center",
                icon: "success",
                title: "Exam Updated SuccessFully",
                showConfirmButton: false,
                timer: 1500
              });


            } else {
              Swal.fire({
                position: "top-center",
                icon: value.result,
                title: value.errMsg,
                showConfirmButton: false,
                timer: 1500
              });
            }
          })

        navigate('/view-exam')
        window.location.reload()

      }
      else {
        fetch(`${uri}/onlineexamapplication/control/addExam`, {
          method: "POST",
          credentials: "include",
          body: JSON.stringify(dataObject),
          headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
        }).then(response => response.json())
          .then(value => {
            console.log(value)
            if (value.result == "success") {
              Swal.fire({
                position: "top-center",
                icon: "success",
                title: "Exam Added SuccessFully",
                showConfirmButton: false,
                timer: 1500
              });
              setTimeout(function () {
                window.location.reload();
              }, 2000)
            } else if (value.result == "error") {
              Swal.fire({
                position: "top-center",
                icon: "Error",
                title: value.errMsg,
                showConfirmButton: false,
                timer: 1500
              });
            }
          })
          .catch(error => console.error('Error:', error));
      }
    }


  };

  //onclick event end.......
  const value = "addExam";
  return (
    <>

      <div className="container pb-5">
        <div className="row pb-5">
          <div className="col-sm-7 offset-md-3 mt-5">
            <div className="card custom-bd-color">
              <div className="card-header text-center">
                {url == false ? <h2>Add Exam</h2> : <h2>Update Exam</h2>}
              </div>
              <div className="card-body">
                <form method="POST" onSubmit={examFormValidate}>
                  <div className="form-group">
                    <label for="examname">Exam Name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="examName"
                      placeholder="Exam Name"
                      name="examName"
                      value={examName}
                      onChange={(e) => { setExamName(e.target.value) }}
                    ></input>
                    <p id="examName_error" className="d-none text-danger"></p>
                  </div>




                  <div className="">
                    <div className="form-group">
                      <label for="description">Description</label>
                      <textarea
                        type="text"
                        className="form-control"
                        id="description"
                        placeholder="Write ur description"
                        name="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                      ></textarea>
                      <p
                        id="description_error"
                        className="d-none text-danger"
                      ></p>
                    </div>
                  </div>


                  <div className="form-group">
                    <div className=" ">
                      <label for="duration">Duration</label>
                      <input
                        type="text"
                        id="duration"
                        className="form-control"
                        placeholder="minutes"
                        name="durationMinutes"
                        min='0'
                        value={durationMinutes}
                        onChange={(e) => setDurationMinutes(e.target.value)}
                      ></input>
                      <p
                        id="durationMinutes_error"
                        className="d-none text-danger"
                      ></p>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col-sm-6 ">
                      <div className="form-group">
                        <label for="creationdate">Creation Date</label>
                        <input
                          type="datetime-local"
                          className="form-control"
                          id="creationdate"
                          name="creationDate"
                          value={creationdate}
                          onChange={(e) => setCreationDate(e.target.value)}
                        ></input>
                        <p
                          id="creationDate_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>

                    <div className="col-sm-5   offset-md-1">
                      <div className="form-group">
                        <label for="expirationdate">Expiration Date</label>
                        <input
                          type="datetime-local"
                          className="form-control"
                          id="expirationdate"
                          name="expirationDate"
                          value={expirationdate}
                          onChange={(e) => { setExpirationDate(e.target.value) }}
                        ></input>
                        <p
                          id="expirationDate_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col-sm-6 offset-md-0">
                      <div className="form-group">
                        <lable >No of Question</lable>
                        <input
                          type="number"
                          className="form-control"
                          id="noof.qa"
                          name="noOfQuestions"
                          min='0'
                          value={noOfQuestions}
                          onChange={(e) => setNoOfquestions(e.target.value)}
                        ></input>
                        <p
                          id="noOfQuestions_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>

                    <div className="col-sm-5 offset-md-1">
                      <label for="passpercentage">PassPercentage</label>
                      <input
                        type="number"
                        id="passpercentage"
                        className="form-control"
                        name="passPercentage"
                        min='0'
                        value={passPercentage}
                        onChange={(e) => setPassPercentage(e.target.value)}
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
                          value={questionsRandomized}
                          onChange={(e) => setQuestionsRandomized(e.target.value)}
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
                          value={answerMust}
                          onChange={(e) => setAnswerMust(e.target.value)}
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
                          name="enableNegativeMark"
                          placeholder="enableNegativeMark"
                          value={enableNegativeMark}
                          onChange={(e) => setEnableNegativeMark(e.target.value)}
                        ></input>
                        <p
                          id="enableNegativeMark_error"
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
                          name="negativeMarkValue"
                          placeholder="negativeMarkValue"
                          min='0'
                          value={negativeMarkValue}
                          onChange={(e) => setNegativeMarkValue(e.target.value)}
                        ></input>
                        <p
                          id="negativeMarkValue_error"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                  <div className="text-center mt-2">
                    <button type="submit" className="btn btn-success">{url == false ? <text>Add Exam</text> : <text>Update Exam</text>}</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div >
      </div >

    </>
  );
}

export default AddExam;