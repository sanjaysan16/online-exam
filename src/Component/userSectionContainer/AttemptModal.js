import React from 'react'

const AttemptModal = () => {
    return (
        <div>
            <div className='modal' id="attemptModal" data-bs-backDrop="static">
                <div className='modal-dialog modal-dialog-centered'>
                    <div class="modal-content">
                        <div className='modal-body px-5 pt-5'>
                            <p className='fs-5'>You are reched your maximun attempt limit </p>

                            <button className='btn btn-outline-danger float-end m-0' data-bs-dismiss="modal">close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default AttemptModal
