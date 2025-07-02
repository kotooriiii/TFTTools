import { motion, Variants } from 'framer-motion';

export const JumpingDots: React.FC = () => {
    const dotVariants: Variants = {
        jump: {
            y: -15,
            transition: {
                duration: 0.5,
                repeat: Infinity,
                repeatType: "mirror",
                ease: "easeInOut",
            },
        },
    }

    return (
        <motion.div
            animate="jump"
            transition={{staggerChildren: -0.15, staggerDirection: -1}}
            className="container"
            style={{
                position: 'absolute',
                bottom: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                gap: '5px'
            }}
        >
            <motion.div className="dot" variants={dotVariants}/>
            <motion.div className="dot" variants={dotVariants}/>
            <motion.div className="dot" variants={dotVariants}/>
            <StyleSheet/>
        </motion.div>
    )

    function StyleSheet()
    {
        return (
            <style>
                {`
            .dot {
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #594b42;
                will-change: transform;
            }
            `}
            </style>
        )
    }
}