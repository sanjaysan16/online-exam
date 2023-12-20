import { Link } from "react-router-dom";

function AddQuestion() {


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
        }
        break;
      case "numberofanswer":
        if (value === "") {
          document
            .getElementById("validatingNumberofanswer")
            .classList.remove("d-none");
          document
            .getElementById("validatingNumberofanswer")
            .classList.add("d-block");
          document.getElementById("validatingNumberofanswer").innerHTML =
            "*Please enter a number of answers";
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
        }
        break;
      case "topicid":
        if (value === "") {
          document
            .getElementById("validatingTopicid")
            .classList.remove("d-none");
          document.getElementById("validatingTopicid").classList.add("d-block");
          document.getElementById("validatingTopicid").innerHTML =
            "*Please enter the topicid";
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
        }
    }
  };
  const onSubmit = (e) => {
    console.log("this is sub,it");
    e.preventDefault();

    const formData = new FormData(e.target);
    

    const answers = Object.fromEntries(formData.entries());
    Object.entries(answers).map(([key, value]) => {
      validatingAnswers(key, value);
    });

    const fetchData = async () => {
      console.log("in fetch");
      const res = await fetch(
        "https://localhost:8443/onlineexamapplication/control/CreateQuestionMaster",
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
      console.log(data);
    };
    fetchData();
  };

  return (
    <>
      <div className="container-fluid pb-5 pt-2 gy-5 ">
        <nav className="arrow" aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <a className="text-decoration-none" href="/">
                Home
              </a>
            </li>
            <li class="breadcrumb-item active">
              <a className="text-decoration-none" href="topics-display">
                TopicsDisplay
              </a>
            </li>
            <li class="breadcrumb-item active">AddQuestion</li>
          </ol>
        </nav>
        <div className="container-fluid pb-5 pt-3 gy-5 ">
          <div className="row">
            <div className="col-sm-7 offset-md-3">
            <div className="card mt-5 card border-dark mb-3">
              <div className="card-header text-center">
                <h2>Add Questions</h2>
              </div>
              <div className="card-body">

                {/* Form Start hear */}

                <form onSubmit={onSubmit}>
                  <div className="form-group">
                    <lable for="questionDetail">Question Detail</lable>
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

                  <div className="form-group">
                    <lable for="option a">Option A</lable>
                    <input
                      type="text"
                      className="form-control"
                      id="option a"
                      name="optionA"
                      placeholder="Enter The Options"
                    ></input>
                    <p
                      id="validatingOptionA"
                      className="d-none text-danger"
                    ></p>
                  </div>
                  <div className="form-group">
                    <lable for="option b">Option B</lable>
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
                    <lable for="option c">Option C</lable>
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
                    <lable for="option d">Option D</lable>
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
                    <lable for="option e">Option E</lable>
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
                  </div>

                  <div className="form-group">
                    <lable for="answer"> Answer</lable>
                    <input
                      type="text"
                      className="form-control"
                      id="answer"
                      name="answer"
                      placeholder="enter the answer"
                    />
                    <p id="validatingAnswer" className="d-none text-danger"></p>
                  </div>

                  
                  <div className='form-group'>
                        <label for='questionType'>QuestionType</label>
                        <select class="form-select" aria-label="Default select example" id='questionType'>
                        <option selected>Open this select menu</option>
                         <option name="singleChoice">Single Choice</option>
                         <option name="multipleChoice">Multiple Choice</option>
                         <option name="fillInTheBlanks">Fill In The Blanks</option>
                         <option name='trueOrFalse'>True or False </option>
                         <option name='detailedAnswer'>Detailed Answer</option>
                         </select>
                     </div>

                     <div className='form-group'>
                        <lable for='difficultyleavel'>DifficultyLeavel</lable>
                        <input type='number' className='form-control' id='difficultyleavel'name='difficultyLeavel'></input>
                     </div>

                  <div className="row">
                    <div className="col-sm-5">
                      <div className="form-group">
                        <lable for="numberofanswer">Number Of Aswer</lable>
                        <input
                          type="number"
                          className="form-control"
                          id="numberofanswer"
                          name="numberOfAnswer"
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
                        <lable for="answervalue">Answer Value</lable>
                        <input
                          type="number"
                          className="form-control"
                          id="answervalue"
                          name="answerValue"
                          placeholder="numberofanswer"
                        />
                        <p
                          id="validatingAnswervalue"
                          className="d-none text-danger"
                        ></p>
                      </div>
                    </div>
                  </div>

                 

                  <div className="row">
                    <div className="col-sm-5">
                      <lable for="topicid">Topic Id</lable>
                      <input
                        type="type"
                        className="form-control"
                        id="topicid"
                        name="topicId"
                        placeholder="Topic Id"
                      />
                      <p
                        className="d-none text-danger"
                        id="validatingTopicid"
                      ></p>
                    </div>

                    <div className="col-sm-5 offset-md-2">
                      <lable for="negativemarkvalue">Negative Mark Value</lable>
                      <input
                        type="number"
                        className="form-control"
                        id="negativemarkvalue"
                        name="negativeMarkValue"
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

                    <a href='view-question'> <button type='button' className="btn btn-warning offset-md-1">ViewQuestion</button></a>
                    </div>
                  </div>
                </form>
              </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default AddQuestion;
