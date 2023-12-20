import React, { useState } from 'react'

const UpdateQuestion = () => {

  var  myKeyValue=window.location.search;

  console.log(myKeyValue,"myKeyValue");
    
  var urlParams=new URLSearchParams(myKeyValue)
    console.log(urlParams,"urlParams");
  var questionId=urlParams.get('questionId')
  var topicId=urlParams.get('topicId')
  console.log(questionId,"questionId")
  console.log(topicId,"topicId")
  function updateQuestion(e){
    e.preventDefault();
    const formData=new FormData(e.target);
    const objectData= Object.fromEntries(formData.entries());
   

    fetch(`https://localhost:8443/onlineexamapplication/control/update-questionmaster?questionId=${questionId}&&topicId=${topicId}`,
    {
        method:'POST',
        credentials:'include',
        body: JSON.stringify(objectData),
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
    }).then(response=>response.json())
    .then(value=>{console.log(value)});
  }
  return (
    <>
    
    <div className='container-fluid pb-5 pt-5 gy-5 '>
    <div className="container-fluid pb-5 pt-5 gy-5">
        <div className="row">
          <div className="col-sm-7  offset-md-3 ">
            <div className="card mt-5 card border-dark mb-3">
              <div className="card-header text-center card border-dark mb-3">
               <h2>Update Question</h2>
              </div>
              <div className="card-body ">
                <form  onSubmit={updateQuestion}>
                <div className='form-group'>
                     <lable for='questiondetail' >Question Detail</lable>
                    <input type='text' className='form-control' id='questiondetail' placeholder='Enter The Options' name='questionDetail' ></input>
                     </div>

                     <div className='form-group'>
                     <lable for='option a' >Option A</lable>
                    <input type='text' className='form-control' id='ption a' placeholder='Enter The Options' name='optionA'></input>
                     </div>
                     <div className='form-group'>
                     <lable for='ption b' >Option B</lable>
                    <input type='text' className='form-control' id='ption b' placeholder='Enter The Options' name='optionB'></input>
                     </div>
                     <div className='form-group'>
                     <lable for='ption c' >Option C</lable>
                    <input type='text' className='form-control' id='ption c' placeholder='Enter The Options' name='optionC'></input>
                     </div>
                     <div className='form-group'>
                     <lable for='ption d' >Option D</lable>
                    <input type='text' className='form-control' id='ption d' placeholder='Enter The Options'name='optionD'></input>
                     </div>

                     <div className='form-group'>
                     <lable for='ption e' >Option E</lable>
                    <input type='text' className='form-control' id='ption e' placeholder='Enter The Options'name='optionE'></input>
                     </div>

                     <div className='form-group'>
                        <lable for='answer'> Answer</lable>
                        <input type='text' className='form-control' id='answer' placeholder='enter the answer' name='answer'>

                        </input>
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
                        <input type='number' className='form-control' id='difficultyleavel'name='difficultyLevel'></input>
                     </div>

                   
                        <div >
                        <div className='form-group'>
                     <lable for='numberofanswer' >Number Of Aswer</lable>
                     <input type='number' className='form-control' id='numberofanswer' placeholder='' name='numberOfAnswer'></input>
                     </div>
                      

                        
                        <div className='form-group'>
                     <lable for='answervalue' >Answer Value</lable>
                     <input type='number' className='form-control' id='answervalue' placeholder=''name='answerValue'></input>
                     </div>
                        </div> 
                    

                     
                

                   
                        
                        <div className='form-group'>
                            <lable for='negativemarkvalue'>Negative Mark Value</lable>
                            <input type='type' className='form-control' id='negativemarkvalue' placeholder='negativemarkvalue' name='negativeMarkValue'>

                            </input>
                        </div>

                        
                        <div className='text-center mt-3'>
                        <button className='btn btn-success' type='submit'>Submit</button>
                        
                      </div>
                     
                    
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
          
                     
{/* 
<div className="container-fluid pb-5 pt-2 gy-5 ">
      
        <div className="container-fluid pb-5 pt-3 gy-5 ">
          <div className="row">
            <div className="col-sm-6 offset-md-3">
              <div className="card-header text-center">
                <h2>Update Questions</h2>
              </div>
              <div className="card-body">

                

                <form >
                  <div className="form-group">
                    <lable for="questionDetail">Question Detail</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="questionDetail"
                      name="questionDetail"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      className="d-none text-danger"
                      id="validatingQuestionDetails"
                    ></p>
                  </div>

                  <div className="form-group">
                    <lable for="option a">Option A</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="option a"
                      name="optionA"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      id="validatingOptionA"
                      className="d-none text-danger"
                    ></p>
                  </div>
                  <div className="form-group">
                    <lable for="option b">Option B</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="option b"
                      name="optionB"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      id="validatingOptionB"
                      className="d-none text-danger"
                    ></p>
                  </div>
                  <div className="form-group">
                    <lable for="option c">Option C</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="option c"
                      name="optionC"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      id="validatingOptionC"
                      className="d-none text-danger"
                    ></p>
                  </div>
                  <div className="form-group">
                    <lable for="option d">Option D</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="option d"
                      name="optionD"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      id="validatingOptionD"
                      className="d-none text-danger"
                    ></p>
                  </div>

                  <div className="form-group">
                    <lable for="option e">Option E</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="option e"
                      name="optionE"
                      placeholder="Enter The Options"
                    ></textarea>
                    <p
                      id="validatingOptionE"
                      className="d-none text-danger"
                    ></p>
                  </div>

                  <div className="form-group">
                    <lable for="answer"> Answer</lable>
                    <textarea
                      type="text"
                      className="form-control"
                      id="answer"
                      name="answer"
                      placeholder="enter the answer"
                    />
                    <p id="validatingAnswer" className="d-none text-danger"></p>
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
                        type="type"
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
                      <button className="btn btn-success" type="submit">
                        Submit
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>     */}
      
      </>
  )
}

export default UpdateQuestion