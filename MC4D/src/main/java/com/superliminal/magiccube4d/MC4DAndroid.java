package com.superliminal.magiccube4d;

import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.DialogFragment;

import com.cyanheron.magiccube4d.gui.ColorPickTableDialogFragment;
import com.cyanheron.magiccube4d.gui.ControlsDialogFragment;
import com.cyanheron.magiccube4d.gui.MC4DConfig;
import com.cyanheron.magiccube4d.gui.MC4DPreferencesManager;
import com.cyanheron.magiccube4d.gui.MenuItemHandler;
import com.cyanheron.magiccube4d.gui.SaveManager;
import com.cyanheron.magiccube4d.gui.StringMapper;
import com.cyanheron.magiccube4d.gui.Utils;
import com.kunmii.custom_dialog_with_tabs.TabbedDialogFragment;
import com.superliminal.magiccube4d.MagicCube.InputMode;
import com.superliminal.util.PropertyManager;
import com.superliminal.util.android.Color;
import com.superliminal.util.android.DialogUtils;
import com.superliminal.util.android.EmailUtils;
import com.superliminal.util.android.Graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

public class MC4DAndroid extends AppCompatActivity {
    private static final int EDGE_LENGTH = 3;
    private static final int FULLY = -1;
    private PuzzleManager mPuzzleManager;
    private History mHist = new History(EDGE_LENGTH);
    private MC4DAndroidView view;
    private MediaPlayer mCorrectSound, mWipeSound, mWrongSound, mFanfareSound;
    private SaveManager mSaveManager;
    private MenuItemHandler[] mMenuItemManagerSave;
    private MenuItemHandler[] mMenuItemManagerLoad;

    private enum ScrambleState { NONE, FEW, FULL }
    private ScrambleState mScrambleState = ScrambleState.NONE;
    private boolean mIsScrambling = false;
    private MC4DPreferencesManager mPreferencesManager;

    private void initMode(int id, final InputMode mode) {
        RadioButton rb = (RadioButton) findViewById(id);
        rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setInputMode(mode);
                int twistor_visibility = mode == InputMode.TWISTING ? View.VISIBLE : View.INVISIBLE;
                findViewById(R.id.L).setVisibility(twistor_visibility);
                findViewById(R.id.R).setVisibility(twistor_visibility);
            }
        });
    }

    private void initTwistor(int id, final int direction) {
        Button b = (Button) findViewById(id);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.twistSelected(direction);
            }
        });
    }

    private void initScrambler(int id, final int scramblechenfrengensen) {
        Button b = (Button) findViewById(id);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scramble(scramblechenfrengensen);
            }
        });
    }

    private static void playSound(MediaPlayer sound) {
        if(sound != null) // Because crash reports say this can happen.
            sound.start();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.main);
        //setContentView(R.layout.main); // For debugging only.
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mPuzzleManager = new PuzzleManager(MagicCube.DEFAULT_PUZZLE, /* MagicCube.DEFAULT_LENGTH */EDGE_LENGTH, new ProgressView());
        view = new MC4DAndroidView(getApplicationContext(), mPuzzleManager, mHist);

        mSaveManager = new SaveManager(this);

        //addContentView(view, params);
        ViewGroup holder = (ViewGroup) findViewById(R.id.puzzle_holder);
        ViewGroup twistors = (ViewGroup) findViewById(R.id.twistors);
        holder.addView(view, params);

        mPreferencesManager = new MC4DPreferencesManager(
            getApplicationContext(), mPuzzleManager.faceColors,
                new Color[]{
                        mPuzzleManager.bgColor,
                        mPuzzleManager.activeColor,
                },
                new Float[]{
                        MagicCube.EYEZ,
                        MagicCube.FACESHRINK,
                        MagicCube.STICKERSHRINK,
                        MagicCube.EYEW
                },
                new String[]{
                        false+""
                },
                (colors, colors2) -> {
                    mPuzzleManager.faceColors = colors;
                    mPuzzleManager.bgColor = colors2[MC4DConfig.Names.BACKGROUND.ordinal()];
                    holder.setBackgroundColor(colors2[MC4DConfig.Names.BACKGROUND.ordinal()].intValue());
                    twistors.setBackgroundColor(colors2[MC4DConfig.Names.BACKGROUND.ordinal()].intValue());

                    mPuzzleManager.activeColor = colors2[MC4DConfig.Names.ACTIVE.ordinal()];
                    // Get property manager set ground
                    view.invalidate();
                },
                adjustments -> {
                    for(Map.Entry<MC4DConfig.Adjustments, MC4DConfig.AdjustmentStruct> entry : MC4DConfig.adjustmentToAdjustmentTabs.entrySet()){
                        PropertyManager.top.setProperty(
                                entry.getValue().property, adjustments[entry.getKey().ordinal()].toString()
                        );
                    }
                    view.invalidate();
                },
                controls -> {
                    mPuzzleManager.useAdvancedControl = controls;
                    view.useAdvancedControl = controls;
                    view.invalidate();
                }
        ); // ColorUtils.generateVisuallyDistinctColors(puzzleDescription.nFaces(), .7f, .1f);

        boolean readOK = readLog(mSaveManager.getCurrentFile(true));
        initMode(R.id.D3, InputMode.ROT_3D);
        initMode(R.id.D4, InputMode.ROT_4D);
        initMode(R.id.twisting, InputMode.TWISTING);
        view.setInputMode(InputMode.ROT_3D);
        initTwistor(R.id.L, 1);
        initTwistor(R.id.R, -1);
        initScrambler(R.id.scramble_1, 1);
        initScrambler(R.id.scramble_2, 2);
        initScrambler(R.id.scramble_3, 3);
        initScrambler(R.id.scramble_full, FULLY);
        Button b = (Button) findViewById(R.id.solve);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                solve();
            }
        });
        // TODO: Call release() on these resources when finished:
        mCorrectSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        mWipeSound = MediaPlayer.create(getApplicationContext(), R.raw.wipe);
        mWrongSound = MediaPlayer.create(getApplicationContext(), R.raw.dink);
        mFanfareSound = MediaPlayer.create(getApplicationContext(), R.raw.fanfare);
        mHist.addHistoryListener(new History.HistoryListener() {
            private boolean adjusting = false;
            @Override
            public void currentChanged() {
                if(adjusting || mSaveManager.lock)
                    return; // Ignore messages from self.
                adjusting = true;

                writeLog(mSaveManager.getCurrentFile(true));
                mMenuItemManagerSave[mSaveManager.getSlot()].update();
                mMenuItemManagerLoad[mSaveManager.getSlot()].update();

                if(mPuzzleManager.isSolved()) {
                    if(!(mScrambleState == ScrambleState.NONE || mIsScrambling))
                        if(mScrambleState == ScrambleState.FULL)
                            playSound(mFanfareSound); // Some poor sap solved full puzzle on Android?? Big reward.
                        else
                            playSound(mCorrectSound); // Small reward.
                    // Reset puzzle state.
                    mScrambleState = ScrambleState.NONE;
                }
                adjusting = false;
            }
        });
    }

//// Shake sensor UI doesn't work all that good so just don't use it.
//private boolean mHasAccelerometer = false;
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
//        List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
//        if(sensors.size() > 0) {
//            mHasAccelerometer = sensorMgr.registerListener(mShakeSensor, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        ((SensorManager) getSystemService(SENSOR_SERVICE)).unregisterListener(mShakeSensor);
//        super.onStop();
//    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        mMenuItemManagerSave = new MenuItemHandler[]{
                new MenuItemHandler(menu.findItem(R.id.save_manual), () -> "Slot "+mSaveManager.getSlot()+" "+mSaveManager.getCurrentInfo(false)),
                new MenuItemHandler(menu.findItem(R.id.save_slot_1), () -> " "+mSaveManager.getInfo(1, true)),
                new MenuItemHandler(menu.findItem(R.id.save_slot_2), () -> " "+mSaveManager.getInfo(2, true)),
                new MenuItemHandler(menu.findItem(R.id.save_slot_3), () -> " "+mSaveManager.getInfo(3, true))
        };

        mMenuItemManagerLoad = new MenuItemHandler[]{
                new MenuItemHandler(menu.findItem(R.id.load_manual), () -> "Slot "+mSaveManager.getSlot()+" "+mSaveManager.getCurrentInfo(false)),
                new MenuItemHandler(menu.findItem(R.id.load_slot_1), () -> " "+mSaveManager.getInfo(1, true)),
                new MenuItemHandler(menu.findItem(R.id.load_slot_2), () -> " "+mSaveManager.getInfo(2, true)),
                new MenuItemHandler(menu.findItem(R.id.load_slot_3), () -> " "+mSaveManager.getInfo(3, true))
        };
        for(MenuItemHandler mih: mMenuItemManagerSave){
            mih.update();
        }
        for(MenuItemHandler mih: mMenuItemManagerLoad){
            mih.update();
        }

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        int slot;
        String appNameStr, html;
        switch(itemId) {
            case R.id.about_orig:
                appNameStr = getString(R.string.app_name) + " ";
                try {
                    appNameStr += "v" + getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName + " ";
                } catch(NameNotFoundException e) {
                    e.printStackTrace();
                }
                html =
                    "<b><big>" + appNameStr + "</big><br>Copyright 2010 by Melinda Green <br>Superliminal Software</b><br>" +
                        "<hr width=\"100%\" align=\"center\" size=\"1\"> <br>" +
                        "This version of Magic Cube 4D is a mobile version of the full-featured desktop application " +
                        "from Superliminal.com. It lets you practice solving slightly randomized puzzles. " +
                        "The hyperstickers (small cubes) are also twisting control axes. " +
                        "Highlight one using the green pointer and click the left or right twist buttons to twist that face. " +
                        "<br><br>Send all questions and comments to <a href=\"mailto:feedback@superliminal.com\">feedback@superliminal.com</a>";
                DialogUtils.showHTMLDialog(this, html);
                break;
            case R.id.scramble_1:
                scramble(1);
                break;
            case R.id.scramble_2:
                scramble(2);
                break;
            case R.id.scramble_3:
                scramble(3);
                break;
            case R.id.scramble_full:
                scramble(FULLY);
                break;
            case R.id.solve_reset:
                scramble(0);
                break;
            case R.id.solve_step:
                solve();
                break;
            case R.id.colors:
                FragmentTransaction ft = Utils.prepareDialog(this);
                DialogFragment cptdf = ColorPickTableDialogFragment.newInstance(this.mPreferencesManager);
                cptdf.show(ft, Utils.DIALOG_TAG);
                break;
            case R.id.controls:
                FragmentTransaction ft5 = Utils.prepareDialog(this);
                DialogFragment ctrldf = ControlsDialogFragment.newInstance(this.mPreferencesManager);
                ctrldf.show(ft5, Utils.DIALOG_TAG);
                break;
            case R.id.adjustments:
                FragmentTransaction ft4 = Utils.prepareDialog(this);
                DialogFragment adf = TabbedDialogFragment.newInstance(this.mPreferencesManager);
                adf.show(ft4, Utils.DIALOG_TAG);
                break;
            case R.id.solve_reset_view:
                view.rotationHandler = new RotationHandler(MagicCube.NICE_VIEW);
                view.invalidate();
                break;
            case R.id.save_manual:
                writeLog(mSaveManager.getCurrentFile(false));

                mMenuItemManagerSave[0].update();
                mMenuItemManagerLoad[0].update();

                break;
            case R.id.save_slot_1 : case R.id.save_slot_2 : case R.id.save_slot_3 :
                slot = 1 + Arrays.stream(new int[]{R.id.save_slot_1, R.id.save_slot_2, R.id.save_slot_3})
                        .boxed().collect(Collectors.toList()).indexOf(itemId);

                writeLog(mSaveManager.getFile(slot, true));
                updateMenuItemHandlers(slot);

                break;
            case R.id.load_manual:
                mSaveManager.lock = true;
                readLog(mSaveManager.getCurrentFile(false));
                mSaveManager.lock = false;
                break;
            case R.id.load_slot_1 :  case R.id.load_slot_2 :  case R.id.load_slot_3 :
                slot = 1 + Arrays.stream(new int[]{R.id.load_slot_1, R.id.load_slot_2, R.id.load_slot_3})
                        .boxed().collect(Collectors.toList()).indexOf(itemId);
                mSaveManager.setSlot(slot);
                mSaveManager.lock = true;
                readLog(mSaveManager.getCurrentFile(true));
                mSaveManager.lock = false;

                mMenuItemManagerSave[0].update();
                mMenuItemManagerLoad[0].update();

                break;
            case R.id.send_log:
                sendLog(mSaveManager.getCurrentFile(true));
                break;
            case R.id.about_mod:
                appNameStr = getString(R.string.app_name) + " ";
                try {
                    appNameStr += "v" + getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName + " ";
                } catch(NameNotFoundException e) {
                    e.printStackTrace();
                }
                html =
                    "<b><big>" + appNameStr + "</big>" +
                    "<hr width=\"100%\" align=\"center\" size=\"1\"> <br>" +
                    "This is a modification of the mobile version of Magic Cube 4D by Melinda Green. " +
                    "The modification reorganises action bar menu, adds colour palette customisation, " +
                    "allows to change view parameters (like in the desktop version) and offers alternative " +
                    "twisting controls. It also offers three save slots with separate autosaves after each twist "+
                    "as well as slots for manual saving to act as an emergency backup.<br>" +
                    "Happy twisting! <br> - Cyan_Heron"
                ;

                DialogUtils.showHTMLDialog(this, html);
                break;
            default:
                break;
        }
        return true;
    } // end onOptionsItemSelected

    private void updateMenuItemHandlers(int slot) {
        mMenuItemManagerSave[0].update();
        mMenuItemManagerLoad[0].update();
        mMenuItemManagerSave[slot].update();
        mMenuItemManagerLoad[slot].update();
    }

    private void solve() {
        mScrambleState = ScrambleState.NONE; // User doesn't get credit for this solve.
        Stack<MagicCube.TwistData> toundo = new Stack<MagicCube.TwistData>();
        for(Enumeration<MagicCube.TwistData> moves = mHist.moves(); moves.hasMoreElements();)
            toundo.push(moves.nextElement());
        while(!toundo.isEmpty()) {
            MagicCube.TwistData last = toundo.pop();
            MagicCube.TwistData inv = new MagicCube.TwistData(last.grip, -last.direction, last.slicemask);
            view.animate(inv, true);
        }
    }

    private void reset() {
        mScrambleState = ScrambleState.NONE;
        mHist.clear();
        mPuzzleManager.resetPuzzleStateNoEvent();
        view.invalidate();
    }

    private void scramble(int scramblechenfrengensen) {
        mIsScrambling = true;
        reset();
        int previous_face = -1;
        PuzzleDescription puzzle = mPuzzleManager.puzzleDescription;
        int length = (int) puzzle.getEdgeLength();
        int totalTwistsNeededToFullyScramble =
                puzzle.nFaces() // needed twists is proportional to nFaces
                    * length // and to number of slices
                    * 2; // and to a fudge factor that brings the 3^4 close to the original 40 twists.
        int scrambleTwists = scramblechenfrengensen == -1 ? totalTwistsNeededToFullyScramble : scramblechenfrengensen;
        Random rand = new Random();
        for(int s = 0; s < scrambleTwists; s++) {
            // select a random grip that is unrelated to the last one (if any)
            int iGrip, iFace, order;
            do {
                // Generate a possible twist.
                iGrip = mPuzzleManager.getRandomGrip();
                iFace = puzzle.getGrip2Face()[iGrip];
                order = puzzle.getGripSymmetryOrders()[iGrip];
            } while( // Keep looking if any of the following conditions aren't met.
            (length > 2 ? (order < 2) : (order < 4)) || // Don't use 360 degree twists, and for 2^4 only allow 90 degree twists.
            (length > 2 && iFace == 7) || // Don't twist the invisible face since Android UI doesn't let the user do that either.
            iFace == previous_face || // Mixes it up better.
            (previous_face != -1 && puzzle.getFace2OppositeFace()[previous_face] == iFace));
            previous_face = iFace;
            int gripSlices = puzzle.getNumSlicesForGrip(iGrip);
            int slicemask = 1; //<<rand.nextInt(gripSlices);
            int dir = rand.nextBoolean() ? -1 : 1;
            // apply the twist to the puzzle state.
            puzzle.applyTwistToState(
                    mPuzzleManager.puzzleState,
                    iGrip,
                    dir,
                    slicemask);
            // and save it in the history.
            MagicCube.Stickerspec ss = new MagicCube.Stickerspec();
            ss.id_within_puzzle = iGrip; // slamming new id. do we need to set the other members?
            ss.face = puzzle.getGrip2Face()[iGrip];
            mHist.apply(ss, dir, slicemask);
            view.invalidate();
            //System.out.println("Adding scramble twist grip: " + iGrip + " dir: " + dir + " slicemask: " + slicemask);
        }
        mHist.mark(History.MARK_SCRAMBLE_BOUNDARY);
        mScrambleState = scramblechenfrengensen == -1 ? ScrambleState.FULL : (
                        scramblechenfrengensen == 0 ? ScrambleState.NONE :
                        ScrambleState.FEW
        );
        mIsScrambling = false;
    } // end scramble

    /*
     * Format:
     * 0 - Magic Number
     * 1 - File Version
     * 2 - Scramble State
     * 3 - Twist Count
     * 4 - Schlafli Product
     * 5 - Edge Length
     */
    private void writeLog(File logfile) {
        String sep = System.getProperty("line.separator");
        int scrambleStateInt = mScrambleState == ScrambleState.FULL ? 2 : mScrambleState == ScrambleState.FEW ? 1 : 0;
        try {
            Writer writer = new FileWriter(logfile);
            writer.write(
                    MagicCube.MAGIC_NUMBER + " " +
                            MagicCube.LOG_FILE_VERSION + " " +
                            scrambleStateInt + " " +
                            mHist.countTwists() + " " +
                            mPuzzleManager.puzzleDescription.getSchlafliProduct() + " " +
                            mPuzzleManager.getPrettyLength());
            writer.write(sep);
            view.getRotations().write(writer);
            //writer.write(sep + puzzle.toString());
            writer.write("*" + sep);
            mHist.write(writer);
            writer.close();
            String filepath = logfile.getAbsolutePath();
            //PropertyManager.userprefs.setProperty("logfile", filepath);
            //setTitle(MagicCube.TITLE + " - " + logfile.getName());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    } // end writeLog()

    private boolean readLog(File logfile) {
        if( ! logfile.exists())
            return false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logfile));
            String firstlineStr = reader.readLine();
            if(firstlineStr == null)
                throw new IOException("Empty log file.");
            String firstline[] = firstlineStr.split(" ");
            if(firstline.length != 6 || !MagicCube.MAGIC_NUMBER.equals(firstline[0]))
                throw new IOException("Unexpected log file format.");
            int readversion = Integer.parseInt(firstline[1]);
            if(readversion != MagicCube.LOG_FILE_VERSION) {
                return false;
            }
            int state = Integer.parseInt(firstline[2]);
            mScrambleState = state == 2 ? ScrambleState.FULL : state == 1 ? ScrambleState.FEW : ScrambleState.NONE;
            int numTwists = Integer.parseInt(firstline[3]);
            String schlafli = firstline[4];
            double initialLength = Double.parseDouble(firstline[5]);
            mPuzzleManager.initPuzzle(schlafli, "" + initialLength,  new ProgressView(), new Graphics.Label(), false);
            mPreferencesManager.reloadAdjustment();
            mPreferencesManager.reloadColors();
            int iLength = (int) Math.round(initialLength);
            view.getRotations().read(reader);
            String title = MagicCube.TITLE;
            for(int c = reader.read(); !(c == '*' || c == -1); c = reader.read())
                ; // read past state data
            if(mHist.read(new PushbackReader(reader))) {
                title += " - " + logfile.getName();
                for(Enumeration<MagicCube.TwistData> moves = mHist.moves(); moves.hasMoreElements();) {
                    MagicCube.TwistData move = moves.nextElement();
                    if(move.grip.id_within_puzzle == -1) {
                        System.err.println("Bad move in MC4DAndroid.initPuzzle: " + move.grip.id_within_puzzle);
                        return false;
                    }
                    mPuzzleManager.puzzleDescription.applyTwistToState(
                            mPuzzleManager.puzzleState,
                            move.grip.id_within_puzzle,
                            move.direction,
                            move.slicemask);
                }
            }
            else
                System.out.println("Error reading puzzle history");
            //setTitle(title);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } // end readLog()

    private boolean sendLog(File logfile) {
        if (!logfile.exists())
            return false;
        String text = "";
        try {
            text = new Scanner(logfile).useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, "CUSTOM MOD: Sending log", Toast.LENGTH_SHORT).show();
        EmailUtils.sendEmail(null, "MagicCube4D log file", text, this);
        return true;
    }

//    private SensorEventListener mShakeSensor = new SensorEventListener() {
//        private static final double STRONG = 12;
//        private long mLastStrongShake = System.currentTimeMillis();
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            float force = (float) Math.sqrt(Vec_h._NORMSQRD3(event.values));
//            long now = System.currentTimeMillis();
//            long dur = now - mLastStrongShake;
//            if(force > STRONG) {
//                if(dur > 1500)
//                    mLastStrongShake = now;
//                else if(dur > 500) {
//                    if(mHist.atScrambleBoundary()) { // Can't undo past scramble boundary.
//                        playSound(mWrongSound);
//                    }
//                    else {
//                        MagicCube.TwistData toUndo = mHist.undo();
//                        if(toUndo != null) {
//                            view.animate(toUndo, false);
//                            view.invalidate();
//                            playSound(mWipeSound);
//                        }
//                    }
//                }
//            }
//        }
//    };

}