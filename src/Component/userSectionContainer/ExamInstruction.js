import React, { useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom';
import useStateRef from 'react-usestateref';

const ExamInstruction = () => {

  const [examId, setExamId, refExamId] = useStateRef('');
  const refCheckboxCheck = useRef();
  const navigate = useNavigate();
  var url = window.location.href.includes('?');


  useEffect(() => {
    if (url) {
      var queryParam = window.location.search;
      var queryParamKeypairs = new URLSearchParams(queryParam);
      const examId = queryParamKeypairs.get('examId');
      setExamId(examId);
    }
  }, []);

  //start exam button action
  const checkboxCheckFunc = () => {

    document.getElementById('i-agree').classList.remove('d-block');
    document.getElementById('i-agree').classList.add('d-none');

    if (refCheckboxCheck.current.checked) {
      navigate(`/taking-exam?examId=${examId}`);
    }
    else {
      document.getElementById('i-agree').classList.remove('d-none');
      document.getElementById('i-agree').classList.add('d-block');
      document.getElementById('i-agree').innerHTML = "*please select agree...!!!";

    }
  }
  return (
    <div className='p-5'>
      <div className='card rounded-0 container w-50 p-3 ' style={{ "background": "#f1f1f1" }}>
        <h1 className='card-title text-center'>INSTRUCTIONS</h1>
        <ol className='card-body'>
          <li>
            Arrange for stable Internet connectivity.
          </li>
          <li>
            Make sure mobile/laptop is fully charged. Power bank for mobile or
            UPS/Inverter for laptop/desktop should be arranged for uninterrupted
            power supply.
          </li>
          <li>
            Close all browsers/tabs before starting the online examination.
          </li>
          <li>
            Do Not Pickup/Receive the Call during the exam if you are giving the exam
            on mobile. This also will be treated as changing the window.
          </li>
          <li>
            To avoid unwanted pop-ups, use of Ad Blocker is recommended.
          </li>

          <li>
            Clear browser cache memory on mobile and laptops. Clear browsing
            history and also delete temp files.
          </li>
          <li>
            Once the exam starts, do not switch to any other window/tab. On doing so,
            your attempt may be considered as malpractice and your exam may get
            terminated.
          </li>
        </ol>

        <label className='float-center'>
          <input type='checkbox' name='agree-check' ref={refCheckboxCheck} />
          i agree</label>

        <p className='d-none text-danger' id="i-agree" >
          *please select agree...!!!
        </p>
        <button className='btn btn-outline-success m-3' onClick={checkboxCheckFunc}>start exam</button>

      </div>
    </div>
  )
}

export default ExamInstruction