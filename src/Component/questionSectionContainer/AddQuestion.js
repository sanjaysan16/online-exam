import React, { Component, useState } from 'react';
import Swal from "sweetalert2";
import useStateRef from "react-usestateref";
import { port, protocol } from '../fetchConst';

function AddQuestion() {

  const uri = `${protocol}://${window.location.hostname}:${port}`;

  const [questionType, setSelectedChoice] = useState('MultipleChoice')
  const [hasError, setHasError, refHasError] = useStateRef(false);


  console.log(questionType, "selectedchoice")

  const questionQueryParam = window.location.search;

  console.log(questionQueryParam, 'asdfghjk')

  const myKeyValue = new URLSearchParams(questionQueryParam);

  console.log(myKeyValue, 'searchparam');

  const topicId = myKeyValue.get('topicId');

  console.log(topicId, 'topic get');
  const validatingAnswers = (key, value) => {

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
  };

  const onSubmit = (e) => {

    console.log("this is sub,it");
    e.preventDefault();
    setHasError(false)
    const formData = new FormData(e.target);


    const answers = Object.fromEntries(formData.entries());
    Object.entries(answers).map(([key, value]) => {
      validatingAnswers(key, value);

      console.log(key)
      console.log(value)
    });
    if (!refHasError.current) {

      const fetchData = async () => {
        console.log("in fetch");
        answers.questionType = questionType;

        const res = await fetch(
          `${uri}/onlineexamapplication/control/CreateQuestionMaster?topicId=${topicId}`,
          {
            method: "POST",
            credentials: "include",
            body: JSON.stringify(answers),
            headers: {
              "Content-Type": "application/json",
              Accept: "application/json",
            },
          }
        );
        const data = await res.json();
        if (data.result == "success") {
          Swal.fire({
            title: "Submited",
            text: "Question Created Successfully",
            icon: "success"

          });
          setTimeout(function () {
            window.location.reload();
          }, 2000)
          // window.location.reload();
        } else if (data.result == "error") {
          Swal.fire({
            position: "top-center",
            icon: "Error",
            title: data.errMsg,
            showConfirmButton: false,
            timer: 1500
          });
        }

      };
      fetchData();
    }


  };

  return (
    <>

      <div className='container w-100% offset-md-0 py-5'>
        <h2 className="fst-italic text-decoration-underline">Select QuestionType</h2>
        <div className='row py-5-'>

          <div className='col-sm-4'>
            <div className='form-group shadow questiontype'>
              <label >QuestionType</label>
              <select class="form-select" aria-label="Default select example" id='questionType' onChange={(e) => setSelectedChoice(e.target.value)}>

                <option name="SingleChoice">SingleChoice</option>
                <option name="MultipleChoice" selected>MultipleChoice</option>
                <option name="FillInTheBlanks">FillInTheBlanks</option>
                <option name='TrueOrFalse'>TrueOrFalse </option>
                <option name='DetailedAnswer'>DetailedAnswers</option>
              </select>
            </div>
          </div>

          {/*form starts hear  */}

          <div className='col-sm-8 '>
            <div className='card card  custom-bd-color mb-3 shadow-lg p-3 mb-5 bg-body rounded'>
              <div className='card-header text-center questioncardheader'>
                <h2>Add Question</h2>
              </div>

              <div className='card-body '>

                <form onSubmit={onSubmit}>

                  <div className="form-group">
                    <lable >Question Detail</lable>
                    <input
                      type="text"
                      className="form-control"
                      id="questionDetail"
                      name="questionDetail"
                      placeholder="Enter The Options"
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
                        <input type='text' className='form-control' name='optionA' id="option a" placeholder='Enter the options' ></input>
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
                        <input type='text' className='form-control' name='optionA' id="option a" placeholder='Enter the options' ></input>
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
                        />
                        <p id="validatingAnswer" className="d-none text-danger"></p>
                      </div>
                    </div> : <></>}

                  <div className='form-group'>
                    <lable >DifficultyLevel</lable>
                    <input type='number' className='form-control' id='difficultylevel' name='difficultyLevel' min='0'></input>

                    <p id="validatingDifficultyLevel" className="d-none text-danger"></p>
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

    </>
  );
}

export default AddQuestion;
