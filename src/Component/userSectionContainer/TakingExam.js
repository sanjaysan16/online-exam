import React, { useEffect, useRef, useState } from 'react'
import useStateRef from 'react-usestateref';
import { port, protocol } from '../fetchConst';

const TakingExam = () => {
  const [examName, setExamName] = useState();
  const [username, setUserName] = useState();


  const [topicList, setTopicList, refTopicList] = useStateRef('');
  const [questionList, setQuestionList] = useState('');
  const [questionType, setQuestionType] = useState();
  const [questionTypeValues, setQuestionTypeValues] = useState();
  const [question, setQuestion, refQuestion] = useStateRef('');
  const [count, setCount] = useState();
  const [countDown, setCountDown] = useState()
  const timerId = useRef()
  useEffect(() => {
    timerId.current = setInterval(() => {
      setCountDown(prev => prev - 1)
    }, 60000)
    return () => clearInterval(timerId.current)
  }, [])
  useEffect(() => {
    if (countDown <= 0) {
      clearInterval(timerId.current)
    }
  }, [countDown])
  const uri = `${protocol}://${window.location.hostname}:${port}`;

  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const examId = urlParams.get('examId')
  useEffect(() => {
    getTopics()
  }, [])

  const getTopics = () => {
    fetch(`${uri}/onlineexamapplication/control/get-topics-of-exam?examId=${examId}`,
      { credentials: "include" })
      .then((response) => response.json())
      .then(data => {
        console.log(data)
        console.log(data.topicAndQuestionForExam);
        setExamName(data.examName);
        setUserName(data.username);
        setTopicList(data.topicAndQuestionForExam)
        console.log(data.topicAndQuestionForExam);

        console.log(refTopicList.current)
        var nameOfFirstTopic = Object.keys(refTopicList.current[0])
        var questionsOfFirstTopic = refTopicList.current[0][nameOfFirstTopic[0]];
        setQuestion(questionsOfFirstTopic[0]);
        console.log("questionsOfFirstTopic", questionsOfFirstTopic)
        console.log(data.durationMinutes);
        setCount(data.durationMinutes);
        console.log(question.questionDetail)
      });
    console.log(topicList);

  }
  const getQuestions = (topics) => {
    console.log("topics", topics);
    fetch(`${uri}/onlineexamapplication/control/get-questions-list?examId=${examId}&topicName=${topics}`,
      { credentials: "include" })
      .then(response => response.json())
      .then(data => {
        console.log(data)
        console.log(data.questionsFromQuestionMaster)
        let iterator = data.questionsFromQuestionMaster.values();
        for (let elements of iterator) {
          setQuestionTypeValues(elements)
          setQuestionList(elements.questionDetail);
          setQuestionType(elements.questionType);
        }
        console.log(data.questionsFromQuestionMaster.questionDetail)
      })
  }
  return (
    <div className='d-flex'>
      <div className='col-sm-3 flex-column min-vh-auto p-3 text-dark z-3 border boder-dark shadow'>
        <h5 className='fw-bolder fs-4 p-0'>
          Topics
          <hr />
        </h5>
        {refTopicList.current && refTopicList.current.map((topic) => {
          return (
            <ul className='nav nav-pills flex-column mb-auto'>
              <li className='nav-item'>
                {/* <a href='/user-dashboard' className='nav-link text-dark'>
                  {Object.keys(topic)}
                 
                </a> */}
                <p onClick={() => { getQuestions(Object.keys(topic)) }}>
                  {Object.keys(topic)}
                </p>
              </li>
            </ul>
          )
        })}

      </div>

      <div className='container col-sm-9 flex-column px-2 pt-5' style={{ "background": "#f1f1f1" }}>
        <div className='pt-2 pb-2'>
          <h4 className='float-start '>Exam Name : {examName}</h4>
          <h6 className='float-end mt-2'>Duration : {count} </h6>
          <h6 className='float-end mt-2 mx-3 mb-2'>UserId : {username}</h6>
        </div>


        <div className='container-fluid border border-2 boder-dark mt-5 p-0'>
          <p className='text-start fw-bold text-wrap text-break'>1. {questionList}</p><br />
          <label>Answers: </label>
          {questionType === "FillInTheBlanks" ?
            <div className='px-5 pb-5 pt-3'>
              <input type='text' className='border border-0'></input>
            </div>
            : (<></>)}
          {questionType === "MultipleChoice" ?
            <div className='px-5 pb-5 pt-3'>
              <input type='checkbox' /><text> {questionTypeValues.optionA}</text><br />
              <input type='checkbox' /><text> {questionTypeValues.optionB}</text><br />
              <input type='checkbox' /><text> {questionTypeValues.optionC}</text><br />
              <input type='checkbox' /><text> {questionTypeValues.optionD}</text><br />
              <input type='checkbox' /><text> {questionTypeValues.optionE}</text><br />
            </div> : (<></>)}
          {questionType === "SingleChoice" ?
            <div className='px-5 pb-5 pt-3'>
              <input type='radio' /><text> {questionTypeValues.optionA}</text><br />
              <input type='radio' /><text> {questionTypeValues.optionB}</text><br />
              <input type='radio' /><text> {questionTypeValues.optionC}</text><br />
              <input type='radio' /><text> {questionTypeValues.optionD}</text><br />
              <input type='radio' /><text> {questionTypeValues.optionE}</text><br />
            </div> : (<></>)}
          {questionType === "DetailedAnswers" ?
            <div className='px-5 pb-5 pt-3'>
              <input type='text' className='border border-0'></input>
            </div> : (<></>)}
          {questionType === "TrueOrFalse" ?
            <div className='px-5 pb-5 pt-3'>
              <input type='radio' /><text>{questionTypeValues.optionA}</text><br />
              <input type='radio' /><text>{questionTypeValues.optionB}</text><br />
            </div> : (<></>)}
        </div>
        <div className='float-end my-3'>
          <button className='btn btn-outline-primary p-2 me-3' type='submit'>Previous</button>
          <button className='btn btn-outline-warning  py-2 px-3 mx-3 ' type='submit'>Skip</button>
          <button className='btn btn-outline-success p-2 px-3 ms-3' type='submit'>Next</button>
        </div>
      </div>
    </div>
  )
}

export default TakingExam