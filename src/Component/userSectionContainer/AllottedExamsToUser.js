import { useNavigate, } from 'react-router-dom';
import { port, protocol } from "../fetchConst";

const AllotedExamsToUser = ({ exam }) => {

  const uri = `${protocol}://${window.location.hostname}:${port}`;
  const navigate = useNavigate();
  
  const navigation=()=>{
     navigate(`/exam-instruction?examId=${exam.examId}`)
  }

  return (
     <div className='border bg-light rounded'>
      <div style={{ "float": "left" }} className='p-5'>
        <h4>{exam.examName}</h4>
      </div>
      <button className="btn-outline-primary p-2 btn mx-3 my-5" type="button"  title="TakeExam" style={{ "float": "right" }} onClick={navigation}>TakeExam</button>
    </div>
  )
}

export default AllotedExamsToUser
