import React, { useEffect, useState } from "react";
import useStateRef from "react-usestateref";
const ViewQuestion = () => {
   
  const [list, setListQuestions, refListQuestions] = useStateRef();
 
  useEffect(() => {
    
    fetchCall();
  },[]);

  function fetchCall() {
    fetch(`https://localhost:8443/onlineexamapplication/control/viewQuestions`)
      .then((res) => res.json())
      .then((data) => {
        var questionList = data.questionList;
       
        setListQuestions(questionList);
       
      });
  }

     
    //  function for delete button
    const deleteQuestion=(id)=>{
      var questionId=id;
      
         fetch(`https://localhost:8443/onlineexamapplication/control/delete?questionId=${questionId}`)
          
        
    }

  return (
    < >

   
  
    <div className="container ">
    <table className='table w-75 offset-2 border border- my-5 '>
     <thead className='table-light  my-5 '>
        <tr className="">
            <th >QuestionId</th>
            <th>QuestionDetail</th>
            <th>TopicId</th>
            <th></th>
            <th></th>
            
        </tr>
     </thead>
     <tbody>
        {list && list.map((listData)=>{
      return(<>
       <tr>
            <td>{listData.questionId}</td>
            <td>{listData.questionDetail}</td>
            <td>{listData.topicId}</td>
            <td>
                <a href={`http://localhost:3000/update-question?questionId=${listData.questionId}&&topicId=${listData.topicId}`} ><button className="btn btn-warning offset-md-2">Update</button></a>
                <button className="btn btn-danger offset-md-1" onClick={()=>deleteQuestion(listData.questionId)} >Delete</button>
            </td>
           
        </tr>
      </>)
        })}
       
     </tbody>
    </table>
    </div>
  
  
    </>
  );
};

export default ViewQuestion;
