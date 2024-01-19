import { useNavigate, } from 'react-router-dom';
import { port, protocol } from "../fetchConst";
import AttemptModal from './AttemptModal';

const AllotedExamsToUser = ({ exam }) => {

  const uri = `${protocol}://${window.location.hostname}:${port}`;
  const navigate = useNavigate();

  const navigation = async (examId) => {

    const userExamMappingFetch = await fetch(`${uri}/onlineexamapplication/control/get-user-exam-mapping?examId=${examId}`, {
      credentials: 'include'
    })
    const userExamMappingDetail = await userExamMappingFetch.json();
  
    console.log("userExamMappingDetail", userExamMappingDetail);

    const getOrCreateUserAttemptFetch = await fetch(`${uri}/onlineexamapplication/control/get-or-create-user-attempt?examId=${examId}`, {
      credentials: 'include'
    })

    const getOrCreateUserAttempt = await getOrCreateUserAttemptFetch.json();

    console.log("getOrCreateUserAttempt", getOrCreateUserAttempt)

    if (getOrCreateUserAttempt.Event === "success") {
      
      navigate(`/exam-instruction?examId=${exam.examId}`)   
    } else {
      if (getOrCreateUserAttempt.Event === "attemptLimitReachecd") {
        document.getElementById('modalCall').click(); 
      }
    }


  }

  return (
    <div className='border bg-light rounded'>
       <button className='d-none' id='modalCall' data-bs-toggle="modal" data-bs-target="#attemptModal"></button>
       <AttemptModal/>
      <div style={{ "float": "left" }} className='p-5'>
        <h4>{exam.examName}</h4>
      </div>
      <button className="btn-outline-primary p-2 btn mx-3 my-5" type="button" title="TakeExam" style={{ "float": "right" }} onClick={() => navigation(exam.examId)}>TakeExam</button>
    </div>
  )
}

export default AllotedExamsToUser
