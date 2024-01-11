import React, { useState, useEffect } from "react";
import useStateRef from "react-usestateref";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { port, protocol } from "../fetchConst";
import WarningModal from "../adminSectionContainer/WarningModal";


const UpdateQuestion = () => {
  const uri = `${protocol}://${window.location.hostname}:${port}`;
  const [data, setData, currentRef] = useStateRef([]);
  const navigate = useNavigate();
  const [questionType, setQuestionType] = useState('')
  const [hasError, setHasError, refHasError] = useStateRef(false);

  const [isAdmin, setIsAdmin] = useState(false)
  const [message, setMessage] = useState('')

  useEffect(() => {
    questionList();
    adminOrUserCheck();
  }, []);

  const adminOrUserCheck = () => {
    fetch(`${uri}/onlineexamapplication/control/check-admin-or-user`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.errMsg == "notAdmin") {
          setIsAdmin(true);
          setMessage(data.message);
          document.getElementById("updatequestionadmincheck").click();
        }
      })
      .catch((error) => console.error("Error:", error));
  };

  const questionList = () => {
    fetch(
      `${uri}/onlineexamapplication/control/update-question?questionId=${questionId}&&topicId=${topicId}`, { credentials: "include" }
    )
      .then((res) => res.json())
      .then((fetchdata) => {
        console.log(
          "fetchdata :: ",
          fetchdata.questionNullCheck
        );
        console.log(fetchdata.questionNullCheck.questionType, "asdfghjkl")
        setQuestionType(fetchdata.questionNullCheck.questionType)
        console.log(questionType, 'updatequestionType===================');
        setData(fetchdata.questionNullCheck);
      })
      .catch(error => console.error('Error:', error))
  };

  function validatefun(key, value) {

    switch (key) {
      case "questionDetail":
        if (value === "") {
          console.log(value);
          document
            .getElementById("validatingQuestionDetails")
            .classList.remove("d-none");
          document
            .getElementById("validatingQuestionDetails")
            .classList.add("d-block");
          document.getElementById("validatingQuestionDetails").innerHTML =
            "*Please enter a question details";
          setHasError(true)
        }
        break;
      case "optionA":
        if (value === "") {
          document
            .getElementById("validatingOptionA")
            .classList.remove("d-none");
          document.getElementById("validatingOptionA").classList.add("d-block");
          document.getElementById("validatingOptionA").innerHTML =
            "*Please enter a value";
          setHasError(true)
        }
        break;
      case "optionB":
        if (value === "") {
          document
            .getElementById("validatingOptionB")
            .classList.remove("d-none");
          document.getElementById("validatingOptionB").classList.add("d-block");
          document.getElementById("validatingOptionB").innerHTML =
            "*Please enter a value";
          setHasError(true)
        }

        break;
      case "optionC":
        if (value === "") {
          document
            .getElementById("validatingOptionC")
            .classList.remove("d-none");
          document.getElementById("validatingOptionC").classList.add("d-block");
          document.getElementById("validatingOptionC").innerHTML =
            "*Please enter a value";
          setHasError(true)
        }
        break;
      case "optionD":
        if (value === "") {
          document
            .getElementById("validatingOptionD")
            .classList.remove("d-none");
          document.getElementById("validatingOptionD").classList.add("d-block");
          document.getElementById("validatingOptionD").innerHTML =
            "*Please enter a value";
          setHasError(true)
        }
        break;
      case "optionE":
        if (value === "") {
          document
            .getElementById("validatingOptionE")
            .classList.remove("d-none");
          document.getElementById("validatingOptionE").classList.add("d-block");
          document.getElementById("validatingOptionE").innerHTML =
            "*Please enter a value";
          setHasError(true)
        }
        break;
      case "answer":
        if (value === "") {
          document
            .getElementById("validatingAnswer")
            .classList.remove("d-none");
          document.getElementById("validatingAnswer").classList.add("d-block");
          document.getElementById("validatingAnswer").innerHTML =
            "*Please enter the answer";
          setHasError(true)
        }
        break;
      case "numAnswers":
        if (value === "") {
          document
            .getElementById("validatingNumberofanswer")
            .classList.remove("d-none");
          document
            .getElementById("validatingNumberofanswer")
            .classList.add("d-block");
          document.getElementById("validatingNumberofanswer").innerHTML =
            "*Please enter a number of answers";
          setHasError(true)
        }
        break;
      case "answervalue":
        if (value === "") {
          document
            .getElementById("validatingAnswervalue")
            .classList.remove("d-none");
          document
            .getElementById("validatingAnswervalue")
            .classList.add("d-block");
          document.getElementById("validatingAnswervalue").innerHTML =
            "*please enter a answervalue";
          setHasError(true)
        }
        break;

      case "negativemarkvalue":
        if (value === "") {
          document
            .getElementById("validatingNegativemarkvalue")
            .classList.remove("d-none");
          document
            .getElementById("validatingNegativemarkvalue")
            .classList.add("d-block");
          document.getElementById("validatingNegativemarkvalue").innerHTML =
            "*Please enter the markvalue";
          setHasError(true)
        }
    }

  }
  var myKeyValue = window.location.search;

  console.log(myKeyValue, "myKeyValue");

  var urlParams = new URLSearchParams(myKeyValue);

  console.log(urlParams, "urlParams");

  var questionId = urlParams.get("questionId");
  var topicId = urlParams.get("topicId");

  console.log(questionId, "questionId");
  console.log(topicId, "topicId");

  function updateQuestion(e) {
    e.preventDefault();
    setHasError(false)
    const formData = new FormData(e.target);
    const objectData = Object.fromEntries(formData.entries());
    Object.entries(objectData).map(([key, value]) => {
      validatefun(key, value)
    });

    if (!refHasError.current) {

      fetch(
        `${uri}/onlineexamapplication/control/update-question?questionId=${questionId}&&topicId=${topicId}`,
        {
          method: "POST",
          credentials: "include",
          body: JSON.stringify(objectData),
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      )
        .then((response) => response.json())
        .then((data) => {
          console.log("vbnm");
          if (data.result == "success") {
            Swal.fire({
              position: "center",
              icon: "success",
              title: "Question Updated Success Fully",
              showConfirmButton: false,
              timer: 1500,

            });
          }
        })
        .catch(error => console.error('Error:', error))
      setTimeout(function () {
        navigate(`/view-question?topicId=${topicId}`)
        window.location.reload();
      }, 1000)
    }

  }
  return (
    <>
      <p
        id="updatequestionadmincheck"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
      ></p>

      <WarningModal message={message} />
      {isAdmin ? (<></>) : (
        <div className='container w-100% mt-5 offset-md-0'>
          <div className='row'>

            {/*form starts hear  */}

            <div className='col-sm-8 offset-md-3'>
              <div className='card card  custom-bd-color mb-3 shadow-lg p-3 mb-5 bg-body rounded'>
                <div className='card-header text-center questioncardheader'>
                  <h2>Update Question</h2>
                </div>

                <div className='card-body '>

                  <form onSubmit={updateQuestion}>

                    <div className="form-group">
                      <lable >Question Detail</lable>
                      <input
                        type="text"
                        className="form-control"
                        id="questionDetail"
                        name="questionDetail"
                        placeholder="Enter The Options"
                        defaultValue={data.questionDetail}
                      ></input>
                      <p
                        className="d-none text-danger"
                        id="validatingQuestionDetails"
                      ></p>
                    </div>

                    {/* multiple choice */}
                    {questionType === "MultipleChoice" ?

                      <div>
                        <div className='form-group'>
                          <lable>optionA</lable>
                          <input type='text' className='form-control' name='optionA' id="option a" placeholder='Enter the options' defaultValue={data.optionA} ></input>
                          <p
                            className="d-none text-danger"
                            id="validatingOptionA"
                          ></p>

                        </div>

                        <div className="form-group">
                          <lable >Option B</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option b"
                            name="optionB"
                            placeholder="Enter The Options"
                            defaultValue={data.optionB}
                          ></input>
                          <p
                            id="validatingOptionB"
                            className="d-none text-danger"
                          ></p>
                        </div>
                        <div className="form-group">
                          <lable>Option C</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option c"
                            name="optionC"
                            placeholder="Enter The Options"
                            defaultValue={data.optionC}
                          ></input>
                          <p
                            id="validatingOptionC"
                            className="d-none text-danger"
                          ></p>
                        </div>
                        <div className="form-group">
                          <lable >Option D</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option d"
                            name="optionD"
                            placeholder="Enter The Options"
                            defaultValue={data.optionD}
                          ></input>
                          <p
                            id="validatingOptionD"
                            className="d-none text-danger"
                          ></p>
                        </div>

                        <div className="form-group">
                          <lable >Option E</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option e"
                            name="optionE"
                            placeholder="Enter The Options"
                            defaultValue={data.optionE}
                          ></input>
                          <p
                            id="validatingOptionE"
                            className="d-none text-danger"
                          ></p>

                          <div className="form-group">
                            <lable > Answer</lable>
                            <input
                              type="text"
                              className="form-control"
                              id="answer"
                              name="answer"
                              placeholder="enter the answer"
                              defaultValue={data.answer}
                            />
                            <p id="validatingAnswer" className="d-none text-danger"></p>
                          </div>
                        </div>
                      </div> : <></>}


                    {/* single choice */}
                    {questionType === "SingleChoice" ?

                      <div>
                        <div className='form-group'>
                          <lable>optionA</lable>
                          <input type='text' className='form-control' name='optionA' id="option a" placeholder='Enter the options' defaultValue={data.optionA} ></input>
                          <p
                            className="d-none text-danger"
                            id="validatingOptionA"
                          ></p>

                        </div>

                        <div className="form-group">
                          <lable >Option B</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option b"
                            name="optionB"
                            placeholder="Enter The Options"
                            defaultValue={data.optionB}
                          ></input>
                          <p
                            id="validatingOptionB"
                            className="d-none text-danger"
                          ></p>
                        </div>
                        <div className="form-group">
                          <lable >Option C</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option c"
                            name="optionC"
                            placeholder="Enter The Options"
                            defaultValue={data.optionC}
                          ></input>
                          <p
                            id="validatingOptionC"
                            className="d-none text-danger"
                          ></p>
                        </div>
                        <div className="form-group">
                          <lable >Option D</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option d"
                            name="optionD"
                            placeholder="Enter The Options"
                            defaultValue={data.optionD}
                          ></input>
                          <p
                            id="validatingOptionD"
                            className="d-none text-danger"
                          ></p>
                        </div>

                        <div className="form-group">
                          <lable >Option E</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="option e"
                            name="optionE"
                            placeholder="Enter The Options"
                            defaultValue={data.optionE}
                          ></input>
                          <p
                            id="validatingOptionE"
                            className="d-none text-danger"
                          ></p>

                          <div className="form-group">
                            <lable > Answer</lable>
                            <input
                              type="text"
                              className="form-control"
                              id="answer"
                              name="answer"
                              placeholder="enter the answer"
                              defaultValue={data.answer}
                            />
                            <p id="validatingAnswer" className="d-none text-danger"></p>
                          </div>
                        </div>
                      </div> : <></>}
                    {/* Fill In The Blanks */}
                    {questionType === "FillInTheBlanks" ?
                      <div>
                        <div className="form-group">
                          <lable > Answer</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="answer"
                            name="answer"
                            placeholder="enter the answer"
                            defaultValue={data.answer}
                          />
                          <p id="validatingAnswer" className="d-none text-danger"></p>
                        </div>
                      </div> : <></>}

                    {/* Detail Answers */}
                    {questionType === "DetailedAnswers" ?
                      <div>
                        <div className="form-group">
                          <lable > Answer</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="answer"
                            name="answer"
                            placeholder="enter the answer"
                            defaultValue={data.answer}
                          />
                          <p id="validatingAnswer" className="d-none text-danger"></p>
                        </div>
                      </div> : <></>}

                    {/* True Or False */}
                    {questionType === "TrueOrFalse" ?
                      <div>
                        <div className='form-check'>
                          <label className="form-check-label" >optionA</label>
                          <input type='text' className="form-control" defaultValue={'True'} ></input>

                        </div>
                        <div className='form-check'>
                          <label className="form-check-label" >optionB</label>
                          <input type='text' className="form-control" defaultValue={'false'}  ></input>

                        </div>
                        <div className="form-group">
                          <lable > Answer</lable>
                          <input
                            type="text"
                            className="form-control"
                            id="answer"
                            name="answer"
                            placeholder="enter the answer"
                            defaultValue={data.answer}
                          />
                          <p id="validatingAnswer" className="d-none text-danger"></p>
                        </div>
                      </div> : <></>}

                    <div className='form-group'>
                      <lable >DifficultyLevel</lable>
                      <input type='number' className='form-control' id='difficultylevel' name='difficultyLevel' min='0' defaultValue={data.dificultyLevel}></input>

                      <p id="validatingAnswer" className="d-none text-danger"></p>
                    </div>

                    <div className="row">
                      <div className="col-sm-5">
                        <div className="form-group">
                          <lable >Number Of Aswer</lable>
                          <input
                            type="number"
                            className="form-control"
                            id="numberofanswer"
                            name="numAnswers"
                            min='0'
                            placeholder="numberofanswer"
                            defaultValue={data.numAnswers}
                          />
                          <p
                            id="validatingNumberofanswer"
                            className="d-none text-danger"
                          ></p>
                        </div>
                      </div>

                      <div className="col-sm-5 offset-md-2">
                        <div className="form-group">
                          <lable >Answer Value</lable>
                          <input
                            type="number"
                            className="form-control"
                            id="answervalue"
                            name="answerValue"
                            min='0'
                            placeholder="numberofanswer"
                            defaultValue={data.answerValue}
                          />
                          <p
                            id="validatingAnswervalue"
                            className="d-none text-danger"
                          ></p>
                        </div>
                      </div>
                    </div>
                    <div className="">
                      <lable>Negative Mark Value</lable>
                      <input
                        type="number"
                        className="form-control"
                        id="negativemarkvalue"
                        name="negativeMarkValue"
                        min='0'
                        placeholder="negativemarkvalue"
                        defaultValue={data.negativeMarkValue}
                      />
                      <p
                        className="d-none text-danger"
                        id="validatingNegativemarkvalue"
                      ></p>
                    </div>


                    <div className="text-center mt-3">
                      <button className="btn btn-success offset-md-1" type="submit">
                        Submit
                      </button>

                      <a href={`view-question?topicId=${topicId}`}> <button type='button' className="btn btn-warning offset-md-1">ViewQuestion</button></a>
                    </div>

                  </form>

                </div>
              </div>
            </div>
          </div>
        </div>

      )}
    </>
  );
};

export default UpdateQuestion;
