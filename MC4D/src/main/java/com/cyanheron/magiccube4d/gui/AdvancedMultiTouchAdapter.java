//package com.cyanheron.magiccube4d.gui;
//
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.superliminal.magiccube4d.MC4DAndroidView;
//import com.superliminal.magiccube4d.MagicCube;
//import com.superliminal.magiccube4d.Vec_h;
//import com.superliminal.util.PropertyManager;
//
//public class AdvancedMultiTouchAdapter {
//
//    private View.OnTouchListener otl;
//    public AdvancedMultiTouchAdapter(MC4DPreferencesManager manager, View v) {
//        this.otl = v.onToucListener;
//    }
//
//    public setAvailability(boolean enabled) {
//
//    }
//
//        this.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // dumpMotionEvent(event);
//                int pid = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
//                long now = event.getEventTime();
//                switch(event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN: // The first pointer down out of however many.
//                        rotationHandler.stopSpinning();
//                        mY = (int) event.getY();
//                        float[] here = new float[]{event.getX(), (int) event.getY()};
//                        if(event.getPointerId(0) == 0) {
//                            lastDragSave = lastDrag0 = lastStart0 = here;
//                            lastDown0 = now;
//                        }
//                        else { // must be the second finger. Does this case happen?
//                            lastDrag1 = lastStart1 = here;
//                            lastDown1 = now;
//                        }
//                        break;
//                    case MotionEvent.ACTION_POINTER_DOWN: // This is the second or later pointer down.
//                        float[]
//                                pos0 = new float[]{event.getX(0), (int) event.getY(0)},
//                                pos1 = new float[]{event.getX(1), (int) event.getY(1)};
//                        if(event.getPointerId(pid) == 0) {
//                            lastDragSave = lastDrag0 = lastStart0 = pos0;
//                            lastDown0 = now;
//                        }
//                        else {
//                            lastDrag1 = lastStart1 = pos1;
//                            lastDown1 = now;
//                        }
//                        float[] diff = new float[2];
//                        Vec_h._VMV2(diff, pos0, pos1);
//                        pinchLastDist = Math.sqrt(Vec_h._NORMSQRD2(diff));
//                        break;
//                    case MotionEvent.ACTION_UP: // The last pointer up out of however many.
//                        float timeDiff = event.getEventTime() - (pid == 0 ? lastDown0 : lastDown1);
//                        float[] curPoint = new float[]{event.getX(), (int) event.getY()};
//                        float[] movement = new float[2];
//                        float[] whichStart = pid == 0 ? lastStart0 : lastStart1;
//                        if(whichStart != null) // Avoid possible NPE from crash reports.
//                            Vec_h._VMV2(movement, whichStart, curPoint);
//                        double moveDist = Math.sqrt(Vec_h._NORMSQRD2(movement));
//                        if(moveDist < getWidth() / 40)
//                            rotationHandler.stopSpinning(); // Treat drags smaller than 1/40th the screen width as a tap.
//                        if(timeDiff < 1000 && moveDist < 5) {
//                            // For rotating-face-to-center via tapping face:
//                            boolean canrot = puzzleManager.canRotateToCenter((int)curPoint[0], (int)curPoint[1], rotationHandler);
//                            if(canrot) {
//                                puzzleManager.clearStickerHighlighting();
//                                puzzleManager.mouseClickedAction(event, rotationHandler, PropertyManager.getFloat("twistfactor", 1), -1, MC4DAndroidView.this);
//                            }
//                            //                         // For twisting via tapping sticker:
//                            //                         int grip = PipelineUtils.pickGrip(
//                            //                         //curPoint[0], curPoint[1]
//                            //                         lastDragSave[0], lastDragSave[1],
//                            //                         puzzleManager.untwistedFrame,
//                            //                         puzzleManager.puzzleDescription);
//                            //                         // The twist might be illegal.
//                            //                         if(grip < 0)
//                            //                             System.out.println("missed");
//                            //                         else {
//                            //                             MagicCube.Stickerspec clicked = new MagicCube.Stickerspec();
//                            //                             clicked.id_within_puzzle = grip; // slamming new id. do we need to set the other members?
//                            //                             clicked.face = puzzleManager.puzzleDescription.getGrip2Face()[grip];
//                            //                             // System.out.println("face: " + clicked.face);
//                            //                             int dir = timeDiff < 250 ? -1 : 1; // (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) ? MagicCube.CCW : MagicCube.CW;
//                            //                             animationQueue.append(new MagicCube.TwistData(clicked, dir, 1), true, false);
//                            //                         }
//                        }
//                        if(pid == 0)
//                            lastDrag0 = null;
//                        else
//                            lastDrag1 = null;
//                        break;
//                    case MotionEvent.ACTION_POINTER_UP: // This is *not* the last pointer up.
//                        if(pid == 0)
//                            lastDrag0 = null;
//                        else
//                            lastDrag1 = null;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        mY = (int) event.getY();
//                        float[] end0 = new float[]{event.getX(0), event.getY(0)};
//                        if(event.getPointerCount() == 1) { // Only rotate while a single finger is down.
//                            float[]
//                                    drag_dir = new float[2],
//                                    last_pos = event.getPointerId(0) == 0 ? lastDrag0 : lastDrag1;
//                            if(last_pos == null)
//                                return true; // This should not happen but it showed in a user's stack trace so test for it.
//                            Vec_h._VMV2(drag_dir, last_pos, end0);
//                            drag_dir[1] *= -1; // in Windows, Y is down, so invert it
//                            // Pick our grip.
//                            if(inputMode == MagicCube.InputMode.TWISTING)
//                                puzzleManager.updateStickerHighlighting(
//                                        (int) event.getX(), (int) (event.getY() - PLANCHETTE_OFFSET_Y - PLANCHETTE_HEIGHT),
//                                        1, false);
//                            if(inputMode == MagicCube.InputMode.ROT_3D || inputMode == MagicCube.InputMode.ROT_4D)
//                                rotationHandler.mouseDragged(drag_dir[0], drag_dir[1], true, false, inputMode == MagicCube.InputMode.ROT_4D);
//                        }
//                        if(event.getPointerId(0) == 0) // Update the first pointer position. (There's always at least one.)
//                            lastDragSave = lastDrag0 = end0;
//                        else { // pointer ID at 0 must be 1 (or greater?)
//                            lastDrag1 = end0;
//                        }
//                        if(event.getPointerCount() > 1) { // There's more than one (maybe assert count == 2?). Update that too.
//                            float[]
//                                    end1 = new float[]{event.getX(1), event.getY(1)},
//                                    newdiff = new float[2];
//                            Vec_h._VMV2(newdiff, end0, end1);
//                            double pinchNewDist = Math.sqrt(Vec_h._NORMSQRD2(newdiff));
//                            pinchSF *= pinchNewDist / pinchLastDist;
//                            pinchSF = Math.max(pinchSF, 0.5);
//                            pinchSF = Math.min(pinchSF, 5.0);
//                            pinchLastDist = pinchNewDist;
//                            lastDrag1 = end1;
//                        }
//                        break;
//                    default:
//                        return false;
//                } // end switch()
//                invalidate();
//                return true;
//            } // end OnTouch
//        }); // OnTouchListener
//    }
//}
