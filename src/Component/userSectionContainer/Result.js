import React, { useEffect, useState } from 'react'
import { port, protocol } from '../fetchConst';
import useStateRef from 'react-usestateref';

const Result = () => {
  const [questionsAttempted,setQuestionsAttempted]=useState();
  const [totalCorrect,setTotalCorrect]=useState();
  const [negativeMarks,setNegativeMarks]=useState();
  const [total,setTotal]=useState();
  const uri = `${protocol}://${window.location.hostname}:${port}`;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const name = urlParams.get('name')
    const examName=urlParams.get('examName')

useEffect(()=>{
  getResult()
},[] 
);

    const getResult = () => {
      fetch(`${uri}/onlineexamapplication/control/get-result?examName=${examName}`, { credentials: "include" })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          setQuestionsAttempted(data.noOfQuestions);
          setTotalCorrect(data.totalCorrect);
          setNegativeMarks(data.totalWrong);
          setTotal(data.total)
        })
    }
    
    
  return (
    <>
    <div className='row pb-5'>

    
    <div className='col-4'>

    </div>
    <div className='col-4 container border border-3 m-5'>
    
        <h1 className='text-center'>Report Card</h1>
        <div className='text-center'>
        <label className='p-2'>Student :  </label>
        <input className='border border-0' defaultValue={name}></input>
        </div>
        <div className='text-center'>
        <label className='p-2'>Exam Name : </label>
        <input className='=border border-0' defaultValue={examName}></input>
        </div>
        {/* <div className='text-center'>
        <label className='p-2'>Performance Id :</label>
        <input className='=border border-0' ></input>
        </div> */}
        <div className='text-center'>
        <label className='p-2'>attempted questions :</label>
        <input className='=border border-0' defaultValue={questionsAttempted}></input>
        </div>
        {/* <div className='text-center'>
        <label className='p-2'>Not attempted questions :</label>
        <input className='=border border-0' ></input>
        </div> */}
        <div className='text-center'>
        <label className='p-2'>Achived Marks :</label>
        <input className='=border border-0' defaultValue={totalCorrect}></input>
        </div>
        <div className='text-center'>
        <label className='p-2'>Negative marks :</label>
        <input className='=border border-0' defaultValue={negativeMarks}></input>
        </div>
        <div className='text-center'>
        <label className='p-2'>Total Marks :</label>
        <input className='=border border-0' defaultValue={total}></input>
        </div>
        
    </div>
    <div className='col-4'>
        
    </div>
    </div>
    </>
    )
}

export default Result