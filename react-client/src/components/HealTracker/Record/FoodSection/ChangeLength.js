import { useState } from 'react';
import styles from './ChangeLength.module.css';

const ChangeLength = ({ setShowLength , setLength , length}) => {
  const [size, setSize] = useState(length);

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <h2>Enter the size</h2>
        <input type="number" value={size} onChange={(e) => setSize(e.target.value)} />
        <button onClick={() => setShowLength(false)}>Cancel</button>
        <button onClick={() => setLength(size)}>Confirm</button>
      </div>
    </div>
  );
};

export default ChangeLength;