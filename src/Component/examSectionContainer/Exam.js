
import Swal from "sweetalert2";
import { Link, json, useNavigate,  } from 'react-router-dom';

const Exam = ({exam}) => {
  


  const navigate =useNavigate();
     
  const editExam=(examId) =>{ 
     navigate(`/add-exam?examId=${examId}`);
     window.location.reload();
  }
  const goTopic=(examId) =>{ 
    navigate(`/view-topic?examId=${examId}`);
    window.location.reload();
 }

  const deleteExam=async(examId)=>{
    try{
    const deleteExamFetch=await fetch('https://localhost:8443/onlineexamapplication/control/delete-exam',{
      method:'POST',
      credentials:'include',
      body:JSON.stringify({examId:examId}),
      headers:{
        'Content-Type':'application/json',
      },
     });
      
     if(!deleteExamFetch.ok){
      throw new Error(`HTTP error! Status:${deleteExamFetch.status}`)
     }
     const deleteExamResult=await deleteExamFetch.json();
     console.log(deleteExamResult);
      window.location.reload();
       
    }
 catch(error){
    console.error(error);
 }
}
  
  return (
    <div className='border bg-light rounded'>
      <div style={{ "float": "left" }} className='p-5'>
        <h4>{exam.examName}</h4>
      </div>
       <button className="btn-outline-primary p-2 btn mx-3 my-5" type="button" data-toggle="tooltip" data-placement="bottom" title="EditExam" style={{ "float": "right" }} onClick={()=>editExam(exam.examId)}> EditExam</button>
        <button className="btn-outline-danger p-2 btn mx-3 my-5" type="button" data-toggle="tooltip" data-placement="bottom" title="DeleteExam" style={{ "float": "right" }} onClick={()=>deleteExam(exam.examId)}>DeleteExam</button>
        <button  className='btn-outline-success btn p-2 mx-3 my-5' type="button" data-toggle="tooltip" data-placement="bottom" title="GoTopic" style={{ "float": "right" }} onClick={()=>goTopic(exam.examId)}> GoTopic</button>

       
    </div>
  )
}

export default Exam
