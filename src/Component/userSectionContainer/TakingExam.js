import React, { useEffect, useRef, useState } from 'react'
import useStateRef from 'react-usestateref';
import { port, protocol } from '../fetchConst';

const TakingExam = () => {
  const [examName, setExamName] = useState();
  const [username, setUserName] = useState();

  const [listOfQuestionsOfTopic, setListOfQuestionsOfTopic, refListOfQuestionsOfTopic] = useStateRef();
  const [currentQuestion, setCurrentQuestion, refCurrentQuestion] = useStateRef();
  const [questionLengthOfTopic, setQuestionLengthOfTopic, refQuestionLengthOfTopic] = useStateRef();
  const [topicList, setTopicList, refTopicList] = useStateRef('');
  const [defaultTopic, setDefaultTopic, refDefaultTopic] = useStateRef('');
  const [currentTopicName, setCurrentTopicName, refCurrentTopicName] = useStateRef('');
  const [topicIndex, setTopicIndex, refTopicIndex] = useStateRef(0);




  //for getNextQuestion button
  const [index, setIndex, refIndex] = useStateRef(0);

  const [questionType, setQuestionType, refQuestionType] = useStateRef();
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
  const uri =`${protocol}://${window.location.hostname}:${port}`;

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
        setExamName(data.examName);
        setUserName(data.username);
        setTopicList(data.topicAndQuestionForExam)
        setDefaultTopic(refTopicList.current[0]);
        setCurrentTopicName(Object.keys(refDefaultTopic.current));
        setListOfQuestionsOfTopic(refDefaultTopic.current[refCurrentTopicName.current])
        setQuestionLengthOfTopic(refListOfQuestionsOfTopic.current.length)
        setCurrentQuestion(refListOfQuestionsOfTopic.current[refIndex.current]);

        setCount(data.durationMinutes);
      });

  }
  console.log("refTopicList.current............",refTopicList.current)
  const getQuestions = (topics,index) => {
        setIndex(0);
        setListOfQuestionsOfTopic(refTopicList.current[index][topics])
        console.log("refSetValues.current", refListOfQuestionsOfTopic.current)
        setQuestionLengthOfTopic(refListOfQuestionsOfTopic.current.length)
        setCurrentQuestion(refListOfQuestionsOfTopic.current[refIndex.current]);
        setQuestionType(refCurrentQuestion.current.questionType);
        setCurrentTopicName(topics);
  }

  const getNextQuestion = (e) => {
    e.preventDefault();
    if (refIndex.current + 1 < refQuestionLengthOfTopic.current) {
      setIndex(refIndex.current + 1);
      setCurrentQuestion(refListOfQuestionsOfTopic.current[refIndex.current]);
      setQuestionType(refCurrentQuestion.current.questionType)
    }

  }
  //  const skipQuestion=()=>{
  //  }
  const getPreviousQuestion = (e) => {
    e.preventDefault();
    if (refIndex.current > 0 && refIndex.current < refQuestionLengthOfTopic.current)
      setIndex(refIndex.current - 1);
    setCurrentQuestion(refListOfQuestionsOfTopic.current[refIndex.current]);
    setQuestionType(refCurrentQuestion.current.questionType)
  }
  return (
    <div className='d-flex'>
      <div className='col-sm-3 flex-column min-vh-auto p-3 text-dark z-3 border boder-dark shadow'>
        <h5 className='fw-bolder fs-4 p-0  pe-auto'>
          Topics
          <hr />
        </h5>
        {console.log("refCurrentTopicName.current ", refCurrentTopicName.current[0])}
        {refTopicList.current && refTopicList.current.map((topic,index) => {
          return (
            <ul className='nav nav-pills flex-column mb-auto'>
              <li className='nav-item'>
                
                <a className={`nav-link ${refCurrentTopicName.current[0] == Object.keys(topic) ? 'active' : 'text-dark'}`} onClick={() => { getQuestions(Object.keys(topic),index) }} style={{ "cursor": "pointer" }} >
                  {Object.keys(topic)}
                </a>
                {/* <a  onClick={() => { getQuestions(Object.keys(topic)) }}>
                  {Object.keys(topic)}
                </a> */}
              </li>
            </ul>
          )
        })}

      </div>
      {console.log("refCurrentQuestion.current", refCurrentQuestion.current)}
      <div className='container col-sm-9 flex-column px-2 pt-5' style={{ "background": "#f1f1f1" }}>
        <div className='pt-2 pb-2'>
          <h4 className='float-start '>Exam Name : {examName}</h4>
          <h6 className='float-end mt-2'>Duration : {count} </h6>
          <h6 className='float-end mt-2 mx-3 mb-2'>UserId : {username}</h6>
        </div>

        <form>


          <div className='container-fluid border border-2 boder-dark mt-5 p-0'>


            <p className='text-start fw-bold text-wrap text-break'>{refIndex.current + 1}.{refCurrentQuestion.current && refCurrentQuestion.current.questionDetail}</p><br />
            <label>Answers: </label>
            {refCurrentQuestion.current && refCurrentQuestion.current.questionType === "FillInTheBlanks" ?
              <div className='px-5 pb-5 pt-3'>
                <input type='text' className='border border-0'></input>
              </div>
              : (<></>)}
            {refCurrentQuestion.current && refCurrentQuestion.current.questionType === "MultipleChoice" ?
              <div className='px-5 pb-5 pt-3'>
                <input type='checkbox' name='optionA' value={refCurrentQuestion.current.optionA} /><text> {refCurrentQuestion.current.optionA}</text><br />
                <input type='checkbox' name='optionB' value={refCurrentQuestion.current.optionB} /><text> {refCurrentQuestion.current.optionB}</text><br />
                <input type='checkbox' name='optionC' value={refCurrentQuestion.current.optionC} /><text>{refCurrentQuestion.current.optionC}</text><br />
                <input type='checkbox' name='optionD' value={refCurrentQuestion.current.optionD} /><text> {refCurrentQuestion.current.optionD}</text><br />
                <input type='checkbox' name='optionE' value={refCurrentQuestion.current.optionE} /><text> {refCurrentQuestion.current.optionE}</text><br />
              </div> : (<></>)}
            {refCurrentQuestion.current && refCurrentQuestion.current.questionType === "SingleChoice" ?
              <div className='px-5 pb-5 pt-3'>
                <input type='radio' name='optionA' value={refCurrentQuestion.current.optionA} /><text> {refCurrentQuestion.current.optionA}</text><br />
                <input type='radio' name='optionB' value={refCurrentQuestion.current.optionB} /><text> {refCurrentQuestion.current.optionB}</text><br />
                <input type='radio' name='optionC' value={refCurrentQuestion.current.optionC} /><text>{refCurrentQuestion.current.optionC}</text><br />
                <input type='radio' name='optionD' value={refCurrentQuestion.current.optionD} /><text> {refCurrentQuestion.current.optionD}</text><br />
                <input type='radio' name='optionE' value={refCurrentQuestion.current.optionE} /><text> {refCurrentQuestion.current.optionE}</text><br />
              </div> : (<></>)}
            {refCurrentQuestion.current && refCurrentQuestion.current.questionType === "DetailedAnswers" ?
              <div className='px-5 pb-5 pt-3'>
                <input type='text' className='border border-0' name='detailedAnswers' />
              </div> : (<></>)}
            {refCurrentQuestion.current && refCurrentQuestion.current.questionType === "TrueOrFalse" ?
              <div className='px-5 pb-5 pt-3'>
                <input type='radio' name='optionA' value={refCurrentQuestion.current.optionA} /><text> {refCurrentQuestion.current.optionA}</text><br />
                <input type='radio' name='optionB' value={refCurrentQuestion.current.optionB} /><text> {refCurrentQuestion.current.optionB}</text><br />
              </div> : (<></>)}
          </div>
          <div className='float-end my-3'>
            <button className='btn btn-outline-primary p-2 me-3' onClick={getPreviousQuestion} >Previous</button>
            {/* <button className='btn btn-outline-warning  py-2 px-3 mx-3 ' onClick={skipQuestion}>Skip</button> */}
            <button className='btn btn-outline-success p-2 px-3 ms-3' onClick={getNextQuestion}>Next</button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default TakingExam